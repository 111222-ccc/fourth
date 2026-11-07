package cn.edu.sicnu.cs.stu.qxl.fifth

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import cn.edu.sicnu.cs.stu.qxl.fifth.PlayerInfo

class MainActivity : AppCompatActivity() {

    private lateinit var tvName: TextView
    private lateinit var tvGender: TextView
    private lateinit var tvPhone: TextView
    private lateinit var tvHighScore: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvName = findViewById(R.id.tv_name)
        tvGender = findViewById(R.id.tv_gender)
        tvPhone = findViewById(R.id.tv_phone)
        tvHighScore = findViewById(R.id.tv_highscore)

        val btnEdit: Button = findViewById(R.id.btn_edit)
        val btnGame: Button = findViewById(R.id.btn_game)

        btnEdit.setOnClickListener {
            val intent = Intent(this, EditInfoActivity::class.java)
            startActivity(intent)
        }

        btnGame.setOnClickListener {
            val intent = Intent(this, CardGameActivity::class.java)
            startActivity(intent)
        }
    }

    //加载右上角菜单
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    //菜单点击事件，把硬编码“确定”改为字符串资源R.string.btn_ok，符合实验要求
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.item_about) {
            AlertDialog.Builder(this)
                .setTitle(R.string.about_title)
                .setMessage(R.string.about_msg)
                .setPositiveButton(R.string.btn_ok, null)
                .show()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        refreshInfoUi()
    }

    private fun refreshInfoUi() {
        tvName.text = getString(R.string.tv_name, PlayerInfo.name)
        tvGender.text = getString(R.string.tv_gender, PlayerInfo.gender)
        tvPhone.text = getString(R.string.tv_phone, PlayerInfo.phone)
        tvHighScore.text = getString(R.string.tv_highscore, PlayerInfo.highScore)
    }
}
