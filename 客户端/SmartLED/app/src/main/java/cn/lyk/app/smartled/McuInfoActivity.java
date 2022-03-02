package cn.lyk.app.smartled;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.gzuliyujiang.colorpicker.ColorPicker;
import com.google.gson.Gson;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.zip.Inflater;

import cn.lyk.app.smartled.config.Config;
import cn.lyk.app.smartled.databinding.ActivityMcuInfoBinding;
import cn.lyk.app.smartled.entity.CmdPacket;
import cn.lyk.app.smartled.entity.Data;
import cn.lyk.app.smartled.entity.Mcu;
import cn.lyk.app.smartled.entity.McuData;
import cn.lyk.app.smartled.entity.Msg;
import cn.lyk.app.smartled.service.McuService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class McuInfoActivity extends AppCompatActivity {
    private ActivityMcuInfoBinding binding;
    private Config config;
    private Mcu mcu;

    private MqttAndroidClient mqttAndroidClient;
    private McuService mcuService;

    private ColorPicker colorPicker;

    private List<Data> dataList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMcuInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        colorPicker = new ColorPicker(this);
        colorPicker.setOnColorPickListener(pickedColor -> {
            binding.textViewColor.setBackgroundColor(pickedColor);
            int color = pickedColor & 0x00FFFFFF;
            sendCmdColor(color);
        });

        binding.textViewColor.setOnClickListener(v -> colorPicker.show());
        binding.switchMode.setOnCheckedChangeListener((buttonView, isChecked) -> sendCmdAuto(isChecked));
        binding.switchState.setOnCheckedChangeListener((buttonView, isChecked) -> sendCmdState(isChecked));


        config = Config.getInstance();
        mcuService = config.retrofit.create(McuService.class);

        Intent intent = getIntent();
        mcu = (Mcu) intent.getSerializableExtra("mcu");

        mqttInit();


        binding.listViewDataList.setAdapter(new BaseAdapter() {
            private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            @Override
            public int getCount() {
                return dataList.size();
            }

            @Override
            public Object getItem(int position) {
                return dataList.get(position);
            }

            @Override
            public long getItemId(int position) {
                return dataList.get(position).getId();
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.mcu_data_row_item, null);
                }
                TextView tvIp = convertView.findViewById(R.id.textViewIp);
                TextView tvData = convertView.findViewById(R.id.textViewData);
                TextView tvTime = convertView.findViewById(R.id.textViewTime);

                Data data = (Data) getItem(position);
                tvIp.setText(data.getIp());
                tvData.setText(data.getValue());
                tvTime.setText(dateFormat.format(data.getCreateTime()));

                return convertView;
            }
        });

        binding.listViewDataList.setOnItemLongClickListener((parent, view, position, id) -> {
            delData(id);
            return true;
        });

        /* 加载第一页的mcu上报的数据 */
        getData(1);
    }


    /**
     * 根据ID删除一条mcu上报的数据
     *
     * @param dataId
     */
    private void delData(Long dataId) {
        mcuService.delData(dataId).enqueue(new Callback<Msg>() {
            @Override
            public void onResponse(Call<Msg> call, Response<Msg> response) {
                if (response.isSuccessful() && response.body().isOk()) {
                    Toast.makeText(McuInfoActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                    getData(1);// 重新加载数据
                }
            }

            @Override
            public void onFailure(Call<Msg> call, Throwable t) {

            }
        });
    }

    /**
     * @param p 页码
     */
    private void getData(int p) {
        mcuService.data(mcu.getId(), p).enqueue(new Callback<List<Data>>() {
            @Override
            public void onResponse(Call<List<Data>> call, Response<List<Data>> response) {
                if (response.isSuccessful()) {
                    List<Data> nextDataList = response.body();
                    if (!nextDataList.isEmpty()) {
                        dataList.clear();
                        dataList.addAll(nextDataList);
                        BaseAdapter adapter = (BaseAdapter) binding.listViewDataList.getAdapter();
                        adapter.notifyDataSetChanged();
                    }
                }

            }

            @Override
            public void onFailure(Call<List<Data>> call, Throwable t) {

            }
        });
    }

    private void mqttInit() {
        String clientId = UUID.randomUUID().toString();
        String subTopic = "mcu/data/" + mcu.getMac();

        mqttAndroidClient = new MqttAndroidClient(getApplicationContext(), config.mqttServer, clientId);
        mqttAndroidClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                try {
                    mqttAndroidClient.subscribe(subTopic, 0);
                    runOnUiThread(() -> {
                        binding.switchMode.setEnabled(true);
                        binding.switchState.setEnabled(true);
                    });
                } catch (MqttException e) {
                }
            }

            @Override
            public void connectionLost(Throwable cause) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                if (!subTopic.equals(topic)) return;
                Gson gson = new Gson();
                McuData mcuData = gson.fromJson(message.toString(), McuData.class);
                runOnUiThread(() -> {
                    binding.switchMode.setChecked(mcuData.getAuto());
                    binding.switchState.setChecked(mcuData.getState());
                    binding.switchState.setEnabled(!mcuData.getAuto());
                    binding.textViewColor.setBackgroundColor(mcuData.getColor() | 0xff000000);
                });
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });

        try {
            mqttAndroidClient.connect();
        } catch (MqttException e) {
        }
    }


    private void sendCmdColor(int color) {
        mcuService.cmd(mcu.getId(), CmdPacket.color(color)).enqueue(new Callback<Msg>() {
            @Override
            public void onResponse(Call<Msg> call, Response<Msg> response) {

            }

            @Override
            public void onFailure(Call<Msg> call, Throwable t) {

            }
        });
    }

    private void sendCmdState(boolean state) {
        mcuService.cmd(mcu.getId(), CmdPacket.state(state)).enqueue(new Callback<Msg>() {
            @Override
            public void onResponse(Call<Msg> call, Response<Msg> response) {

            }

            @Override
            public void onFailure(Call<Msg> call, Throwable t) {

            }
        });
    }

    private void sendCmdAuto(boolean auto) {
        binding.switchState.setEnabled(!auto);
        mcuService.cmd(mcu.getId(), CmdPacket.auto(auto)).enqueue(new Callback<Msg>() {
            @Override
            public void onResponse(Call<Msg> call, Response<Msg> response) {

            }

            @Override
            public void onFailure(Call<Msg> call, Throwable t) {

            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
        colorPicker = null;
        mcuService = null;
        if (mqttAndroidClient != null) {
            mqttAndroidClient.close();
            mqttAndroidClient = null;
        }
        mcu = null;
        config = null;
    }
}