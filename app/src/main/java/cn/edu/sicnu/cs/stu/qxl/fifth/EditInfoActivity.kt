package cn.edu.sicnu.cs.stu.qxl.fifth

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import cn.edu.sicnu.cs.stu.qxl.fifth.PlayerInfo

class EditInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_info)

        val etName: EditText = findViewById(R.id.et_name)
        val etGender: EditText = findViewById(R.id.et_gender)
        val etPhone: EditText = findViewById(R.id.et_phone)
        val btnSave: Button = findViewById(R.id.btn_save)
        val btnBack: Button = findViewById(R.id.btn_back)

        // 回显已有信息
        etName.setText(PlayerInfo.name)
        etGender.setText(PlayerInfo.gender)
        etPhone.setText(PlayerInfo.phone)

        btnSave.setOnClickListener {
            PlayerInfo.name = etName.text.toString()
            PlayerInfo.gender = etGender.text.toString()
            PlayerInfo.phone = etPhone.text.toString()
            finish()
        }
        btnBack.setOnClickListener {
            finish()
        }
    }
}
