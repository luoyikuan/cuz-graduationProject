package cn.lyk.app.smartled.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import cn.lyk.app.smartled.LoginOrRegisterActivity;
import cn.lyk.app.smartled.MainActivity;
import cn.lyk.app.smartled.R;
import cn.lyk.app.smartled.config.Config;
import cn.lyk.app.smartled.databinding.FragmentMeBinding;
import cn.lyk.app.smartled.entity.Msg;
import cn.lyk.app.smartled.service.UserService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MeFragment extends Fragment {
    private FragmentMeBinding binding;

    private Config config = Config.getInstance();
    private SharedPreferences sharedPref;
    private UserService userService;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPref = getActivity().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        userService = config.retrofit.create(UserService.class);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        binding.btnLogout.setOnClickListener(v -> doLogout());
        binding.btnChangeLoginPwd.setOnClickListener(v -> doChangePwd());
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void doLogout() {
        Call<Msg> logoutCall = userService.logout();
        logoutCall.enqueue(new Callback<Msg>() {
            @Override
            public void onResponse(Call<Msg> call, Response<Msg> response) {
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.clear();
                editor.apply();

                config.login = false;

                Intent intent = new Intent(MeFragment.this.getActivity(), LoginOrRegisterActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<Msg> call, Throwable t) {
                Toast.makeText(MeFragment.this.getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void doChangePwd() {
        String oldLoginPwd = binding.editTextOldLoginPwd.getText().toString().trim();
        String newLoginPwd = binding.editTextNewLoginPwd.getText().toString().trim();
        String twoLoginPwd = binding.editTextTwoLoginPwd.getText().toString().trim();

        if (oldLoginPwd.isEmpty()) {
            Toast.makeText(this.getActivity(), "旧密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        if (newLoginPwd.isEmpty()) {
            Toast.makeText(this.getActivity(), "新密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newLoginPwd.equals(twoLoginPwd)) {
            Toast.makeText(this.getActivity(), "2次密码输入不一致", Toast.LENGTH_SHORT).show();
            return;
        }

        Call<Msg> changePwdCall = userService.changePwd(oldLoginPwd, newLoginPwd);
        changePwdCall.enqueue(new Callback<Msg>() {
            @Override
            public void onResponse(Call<Msg> call, Response<Msg> response) {
                Msg msg = response.body();
                if (response.isSuccessful() && msg.isOk()) {
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("loginPwd", newLoginPwd);
                    editor.apply();
                    Toast.makeText(MeFragment.this.getActivity(), "修改成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MeFragment.this.getActivity(), "修改失败!请确认旧密码是否正确", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Msg> call, Throwable t) {
                Toast.makeText(MeFragment.this.getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}