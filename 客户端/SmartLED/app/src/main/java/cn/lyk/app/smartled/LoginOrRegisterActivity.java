package cn.lyk.app.smartled;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import cn.lyk.app.smartled.config.Config;
import cn.lyk.app.smartled.databinding.ActivityLoginOrRegisterBinding;
import cn.lyk.app.smartled.entity.Msg;
import cn.lyk.app.smartled.entity.User;
import cn.lyk.app.smartled.service.UserService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginOrRegisterActivity extends AppCompatActivity {
    private ActivityLoginOrRegisterBinding binding;

    private Config config;
    private SharedPreferences sharedPref;

    private UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginOrRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        config = Config.getInstance();
        userService = config.retrofit.create(UserService.class);
        sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        String localLoginId = sharedPref.getString("loginId", null);
        String localLoginPwd = sharedPref.getString("loginPwd", null);
        if (localLoginId != null || localLoginPwd != null) {
            doLogin(localLoginId, localLoginPwd);
        }


        binding.btnLogin.setOnClickListener(v -> {
            String loginId = binding.editTextLoginId.getText().toString();
            String loginPwd = binding.editTextLoginPwd.getText().toString();
            doLogin(loginId, loginPwd);
        });
        binding.btnRegister.setOnClickListener(v -> doRegister());
    }

    private void doLogin(String loginId, String loginPwd) {
        User user = new User();
        user.setLoginId(loginId.trim());
        user.setLoginPwd(loginPwd.trim());

        Call<Msg> loginCall = userService.login(user);
        loginCall.enqueue(new Callback<Msg>() {
            @Override
            public void onResponse(Call<Msg> call, Response<Msg> response) {
                Msg msg = response.body();
                if (response.isSuccessful() && msg.isOk()) {
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("loginId", user.getLoginId());
                    editor.putString("loginPwd", user.getLoginPwd());
                    editor.apply();

                    config.login = true;

                    Intent intent = new Intent(LoginOrRegisterActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginOrRegisterActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Msg> call, Throwable t) {
                Toast.makeText(LoginOrRegisterActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void doRegister() {
        User user = new User();
        user.setLoginId(binding.editTextLoginId.getText().toString().trim());
        user.setLoginPwd(binding.editTextLoginPwd.getText().toString().trim());

        Call<Msg> registerCall = userService.register(user);
        registerCall.enqueue(new Callback<Msg>() {
            @Override
            public void onResponse(Call<Msg> call, Response<Msg> response) {
                Msg msg = response.body();
                if (response.isSuccessful() && msg.isOk()) {
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("loginId", user.getLoginId());
                    editor.putString("loginPwd", user.getLoginPwd());
                    editor.apply();

                    config.login = true;

                    Intent intent = new Intent(LoginOrRegisterActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginOrRegisterActivity.this, "注册失败,用户名可能重复", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Msg> call, Throwable t) {
                Toast.makeText(LoginOrRegisterActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}