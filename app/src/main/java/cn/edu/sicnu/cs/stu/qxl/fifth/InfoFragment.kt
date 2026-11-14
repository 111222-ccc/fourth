package cn.edu.sicnu.cs.stu.qxl.fifth

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import java.io.*

data class PersonInfo(
    var name: String = "",
    var studentId: String = "",
    var gender: String = "",
    var phone: String = "",
    var college: String = "",
    var major: String = "",
    var hobby: String = "",
    var bio: String = ""
) : Serializable

class InfoFragment : Fragment() {

    private lateinit var etName: EditText
    private lateinit var etStudentId: EditText
    private lateinit var etGender: EditText
    private lateinit var etPhone: EditText
    private lateinit var etCollege: EditText
    private lateinit var etMajor: EditText
    private lateinit var etHobby: EditText
    private lateinit var etBio: EditText
    private lateinit var btnSave: Button

    companion object {
        @JvmStatic
        fun newInstance() = InfoFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        etName = view.findViewById(R.id.et_name)
        etStudentId = view.findViewById(R.id.et_student_id)
        etGender = view.findViewById(R.id.et_gender)
        etPhone = view.findViewById(R.id.et_phone)
        etCollege = view.findViewById(R.id.et_college)
        etMajor = view.findViewById(R.id.et_major)
        etHobby = view.findViewById(R.id.et_hobby)
        etBio = view.findViewById(R.id.et_bio)
        btnSave = view.findViewById(R.id.btn_save_info)

        //读取本地信息回填
        val info = loadPersonInfo()
        info?.let {
            etName.setText(it.name)
            etStudentId.setText(it.studentId)
            etGender.setText(it.gender)
            etPhone.setText(it.phone)
            etCollege.setText(it.college)
            etMajor.setText(it.major)
            etHobby.setText(it.hobby)
            etBio.setText(it.bio)
        }

        btnSave.setOnClickListener {
            val person = PersonInfo(
                name = etName.text.toString(),
                studentId = etStudentId.text.toString(),
                gender = etGender.text.toString(),
                phone = etPhone.text.toString(),
                college = etCollege.text.toString(),
                major = etMajor.text.toString(),
                hobby = etHobby.text.toString(),
                bio = etBio.text.toString()
            )
            savePersonInfo(person)
        }
    }

    //保存序列化文件
    private fun savePersonInfo(info: PersonInfo) {
        try {
            val output: FileOutputStream? = requireActivity().openFileOutput("info.dat", Activity.MODE_PRIVATE)
            val objOut = ObjectOutputStream(output)
            objOut.writeObject(info)
            objOut.close()
            output?.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //读取
    private fun loadPersonInfo(): PersonInfo? {
        return try {
            val input: FileInputStream? = requireActivity().openFileInput("info.dat")
            val objIn = ObjectInputStream(input)
            val data = objIn.readObject() as PersonInfo
            objIn.close()
            input?.close()
            data
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
