package com.my.cloudcontact.dialog;

import com.my.cloudcontact.R;
import com.my.cloudcontact.util.TostHelper;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by admin on 15/11/24.
 */
public class InputDialog extends Dialog {



    private EditText etText1, etText2, etUserNum,etDeviceNum;
    private InputDialogInterface confirmListener;

    public void setConfirmListener(InputDialogInterface confirmListener) {
        this.confirmListener = confirmListener;
    }

    public EditText getText2() {
        return etText2;
    }

    public EditText getText1() {
        return etText1;
    }
    public EditText getUserNum() {
        return etUserNum;
    }
    public TextView getConfirm() {
        return btnModify;
    }

    public interface InputDialogInterface {
        public void onBtnClick(String et1, String et2, String mUserNum,String etDeviceNum);

    }

    InputDialogInterface dlgClickListener;
    Button btnModify;

    public InputDialog(Context context,int theme) {
        super(context,theme);

        setContentView(R.layout.dialog_input);
        etText1 = (EditText) findViewById(R.id.etRegUser);
        etText2 = (EditText) findViewById(R.id.etRegPwd);
        etUserNum = (EditText) findViewById(R.id.etUserNum);
        btnModify = (Button)findViewById(R.id.btnRegModify);
        etDeviceNum = (EditText)findViewById(R.id.etDeviceNum);
        
        btnModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textUp = etText1.getText().toString();
                String textDown = etText2.getText().toString();
                String mUserNum = etUserNum.getText().toString();
                String mDeviceNum = etDeviceNum.getText().toString();
                if (TextUtils.isEmpty(textUp)) {
                	TostHelper.ToastLg("用户名不能为空", v.getContext());
        			return;
        		}
                if (TextUtils.isEmpty(textDown)) {
                	TostHelper.ToastLg("密码不能为空", v.getContext());
        			return;
        		}
                if (TextUtils.isEmpty(mUserNum)) {
                	TostHelper.ToastLg("管理人不能为空", v.getContext());
        			return;
        		}
                if (TextUtils.isEmpty(mDeviceNum)) {
                	TostHelper.ToastLg("设备编号不能为空", v.getContext());
        			return;
        		}
                if(confirmListener != null){
                    confirmListener.onBtnClick(textUp, textDown, mUserNum,mDeviceNum);
                }
            }
        });
    }

}
