package cn.lyk.app.smartled.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import cn.lyk.app.smartled.McuInfoActivity;
import cn.lyk.app.smartled.R;
import cn.lyk.app.smartled.config.Config;
import cn.lyk.app.smartled.databinding.FragmentRobotBinding;
import cn.lyk.app.smartled.entity.Mcu;
import cn.lyk.app.smartled.entity.Msg;
import cn.lyk.app.smartled.service.McuService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RobotFragment extends Fragment {
    private FragmentRobotBinding binding;

    private Config config;
    private McuService mcuService;

    private McuListAdapter mcuListAdapter = new McuListAdapter(new ArrayList<>());

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        config = Config.getInstance();
        mcuService = config.retrofit.create(McuService.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRobotBinding.inflate(inflater, container, false);

        binding.mcuListView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        binding.mcuListView.setAdapter(mcuListAdapter);
        binding.btnAddMcu.setOnClickListener(v -> doBindMcu());
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        getMcuList();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void doBindMcu() {
        String mac = binding.editTextMac.getText().toString().trim();
        String token = binding.editTextToken.getText().toString().trim();
        Mcu mcu = new Mcu();
        mcu.setMac(mac);
        mcu.setToken(token);

        mcuService.add(mcu).enqueue(new Callback<Msg>() {
            @Override
            public void onResponse(Call<Msg> call, Response<Msg> response) {
                Msg msg = response.body();
                if (response.isSuccessful() && msg.isOk()) {
                    Toast.makeText(RobotFragment.this.getContext(), "绑定成功", Toast.LENGTH_SHORT).show();
                    getMcuList();
                } else {
                    Toast.makeText(RobotFragment.this.getContext(), "绑定失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Msg> call, Throwable t) {
                Toast.makeText(RobotFragment.this.getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void removeMcu(Long mcuId, int position) {
        mcuService.delMcu(mcuId).enqueue(new Callback<Msg>() {
            @Override
            public void onResponse(Call<Msg> call, Response<Msg> response) {
                if (response.isSuccessful() && response.body().isOk()) {
                    Toast.makeText(RobotFragment.this.getContext(), "解绑成功", Toast.LENGTH_SHORT).show();
                    mcuListAdapter.localDataList.remove(position);
                    //删除动画
                    mcuListAdapter.notifyItemRemoved(position);
                    mcuListAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<Msg> call, Throwable t) {

            }
        });
    }

    private void getMcuList() {
        mcuService.list().enqueue(new Callback<List<Mcu>>() {
            @Override
            public void onResponse(Call<List<Mcu>> call, Response<List<Mcu>> response) {
                if (response.isSuccessful()) {
                    List<Mcu> mcuList = response.body();
                    mcuListAdapter.setLocalDataList(mcuList);
                    mcuListAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Mcu>> call, Throwable t) {
                Toast.makeText(RobotFragment.this.getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class McuListAdapter extends RecyclerView.Adapter<McuListAdapter.ViewHolder> {
        private List<Mcu> localDataList;

        public void setLocalDataList(List<Mcu> localDataList) {
            this.localDataList = localDataList;
        }

        public McuListAdapter(List<Mcu> dataList) {
            localDataList = dataList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.mcu_row_item, parent, false);

            ViewHolder vh = new ViewHolder(view);

            view.setOnClickListener(v -> {
                int position = vh.getAdapterPosition();
                Mcu mcu = localDataList.get(position);

                Intent intent = new Intent(RobotFragment.this.getContext(), McuInfoActivity.class);
                intent.putExtra("mcu", mcu);
                startActivity(intent);
            });

            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Mcu mcu = localDataList.get(position);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            holder.rootView.setOnLongClickListener(v -> {
                removeMcu(mcu.getId(), position);
                return true;
            });

            holder.getMacTextView().setText(mcu.getMac());
            holder.getCreateTimeTextView().setText(dateFormat.format(mcu.getCreateTime()));
            holder.getLastTimeTextView().setText(dateFormat.format(mcu.getLastTime()));
        }

        @Override
        public int getItemCount() {
            return localDataList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private final TextView textViewMac;
            private final TextView textViewCreateTime;
            private final TextView textViewLastTime;
            private final View rootView;


            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                rootView = itemView;
                textViewMac = itemView.findViewById(R.id.textViewMac);
                textViewCreateTime = itemView.findViewById(R.id.textViewCreateTime);
                textViewLastTime = itemView.findViewById(R.id.textViewLastTime);
            }

            public TextView getMacTextView() {
                return textViewMac;
            }

            public TextView getCreateTimeTextView() {
                return textViewCreateTime;
            }

            public TextView getLastTimeTextView() {
                return textViewLastTime;
            }
        }
    }
}
