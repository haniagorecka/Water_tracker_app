package com.example.watertrackerapp
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.PropertyName
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

data class Date (@get: PropertyName("date") @set: PropertyName("date") var date: String = "",
            @get: PropertyName("amount") @set: PropertyName("amount") var amount: Int = 0)
class RecycleViewActivity: BaseActivity() {
    lateinit var GoToMainButton:Button
    var listOfDates = mutableListOf<RecycleViewData>()
    lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycle)
        val database = Firebase.firestore
        val databaseOp = DatabaseOperations(database)
        val uID = intent
        val userID = uID.getStringExtra("uID")
        GoToMainButton = findViewById(R.id.BackButton)
        if(userID != null)
        {
            lifecycleScope.launch{
                val snapshot = database.collection("Users")
                    .document(userID).collection("WaterIntake")
                    .get().await()

               snapshot.forEach {

                   val date =  it.toObject(Date::class.java)
                   if(date!=null)
                   {
                       val data: RecycleViewData = RecycleViewData(date.date, date.amount.toString())
                       listOfDates.add(data)
                   }
                   else
                   {
                       throw Exception("Exceprion!!!!!!")
                   }


               }
                listOfDates = listOfDates.asReversed()
                recyclerView = findViewById(R.id.WaterRecycleVIew)
                recyclerView.layoutManager = LinearLayoutManager(this@RecycleViewActivity)
                recyclerView.setHasFixedSize(true)
                recyclerView.adapter = RecycleViewAdapter(listOfDates)
            }
        }
        else throw Exception("WTFFFF!!!")

//        val DateList = mutableListOf<String>("1", "2", "3", "1", "2", "3", "1", "2", "3")
//        val AmountList = mutableListOf<String>("A", "B", "C", "D", "A", "B", "C", "D", "E")
//       for(i in 0..DateList.size-1){
//            val dateItem: RecycleViewData = RecycleViewData(DateList[i], AmountList[i])
//            listOfDates.add(dateItem)
//        }


        GoToMainButton.setOnClickListener()
        {
            lifecycleScope.launch {
                val user = getUserInfoFromDB()
                if (user != null && user.data_set) {
                    goToMainActivity(user)
                }
            }
        }
    }

    private fun goToMainActivity(user: User) {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("uID", user.email)
            putExtra("userAge", user.age)
            putExtra("userWeight", user.weight)
            putExtra("userHeight", user.height)
        }
        startActivity(intent)
        finish()
    }

    private suspend fun getUserInfoFromDB(): User? {
        val database = Firebase.firestore
        val databaseOp = DatabaseOperations(database)
        val userID = intent.getStringExtra("uID")
        return if (userID != null) {
            withContext(Dispatchers.IO) {
                databaseOp.getUser(userID)
            }
        } else {
            null
        }
    }
}