package github.earth

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.StorageReference
import github.earth.models.Tutorial
import github.earth.models.User
import kotlinx.coroutines.tasks.await
import java.util.*
import kotlin.collections.ArrayList

class TutorialRepository(

    private val auth: FirebaseAuth,
    private val storageRef: StorageReference,
    private val firestoreRef: FirebaseFirestore,

) {


    //занос FireStore

    suspend fun loginUser(email: String, password: String): AuthResult =
        auth.signInWithEmailAndPassword(email, password).await()

    suspend fun registerUser(email: String, password: String): AuthResult =
        auth.createUserWithEmailAndPassword(email, password).await()


    suspend fun uploadProfileImage(photoUri: Uri): Uri {
        val filename = UUID.randomUUID().toString()
        val ref = storageRef.child("profileImages/$filename")
        ref.putFile(photoUri)
            .await()
        return ref.downloadUrl.await()
    }

    suspend fun saveUserToFirestore(user: User) {
        val userCollectionRef = firestoreRef.collection("users")
        userCollectionRef.add(user).await()
    }

    suspend fun getCurrentlyLoggedInUserDetails(): User {
        val currentUserEmail = auth.currentUser?.email
        var currentUser: User? = null

        val userCollectionRef = firestoreRef.collection("users")
        val querySnapshot = userCollectionRef
            .whereEqualTo("email", currentUserEmail)
            .get().await()

        for (document in querySnapshot.documents) {
            currentUser = document.toObject<User>()
        }
        return currentUser!!
    }





    suspend fun uploadTutorialImage(uri: Uri): Uri {
        val filename = UUID.randomUUID().toString()
        val ref = storageRef.child("tutorialImages/$filename")
        ref.putFile(uri)
            .await()
        return ref.downloadUrl.await()
    }

    suspend fun saveTutorialToFirestore(tutorial: Tutorial) {
        val tutorialCollectionRef = firestoreRef.collection("tutorials")
        tutorialCollectionRef.add(tutorial).await()
    }

    fun getAllTutorials(): MutableLiveData<ArrayList<Tutorial>> {
        val tutorialCollectionRef = firestoreRef.collection("tutorials")
            .orderBy("timestamp", Query.Direction.DESCENDING)
        val _tutorials = MutableLiveData<ArrayList<Tutorial>>()

        tutorialCollectionRef.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            firebaseFirestoreException?.let {
                throw it
            }
            querySnapshot?.let {
                val tutorialList = ArrayList<Tutorial>()
                for (document in querySnapshot.documents) {
                    val tutorial = document.toObject<Tutorial>()
                    tutorialList.add(tutorial!!)
                }
                _tutorials.value = tutorialList
            }
        }
        return _tutorials
    }

    suspend fun updateProfile(currentUser: User, updatedUserMap: Map<String, Any>) {
        val userCollectionRef = firestoreRef.collection("users")
        val userQuery = userCollectionRef
            .whereEqualTo("email", currentUser.email)
            .get()
            .await()

        if (userQuery.documents.isNotEmpty()) {
            for (document in userQuery.documents) {
                userCollectionRef.document(document.id).set(
                    updatedUserMap, SetOptions.merge()
                ).await()
            }

            updateTutorials(currentUser, updatedUserMap)
        }
    }

    private suspend fun updateTutorials(currentUser: User, updatedUserMap: Map<String, Any>) {
        val tutorialCollectionRef = firestoreRef.collection("tutorials")
        val tutorialQuery = tutorialCollectionRef
            .whereEqualTo("email", currentUser.email)
            .get()
            .await()

        for (document in tutorialQuery.documents) {
            tutorialCollectionRef.document(document.id).set(
                updatedUserMap, SetOptions.merge()
            ).await()
        }
    }
}
