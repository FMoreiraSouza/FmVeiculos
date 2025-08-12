package com.example.fmveiculos.data.repository

import com.example.fmveiculos.data.model.UserInfoModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthRepository {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    suspend fun loginUser(email: String, password: String): Boolean {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            true
        } catch (_: Exception) {
            false
        }
    }

    suspend fun registerUser(email: String, password: String, cpf: String): Pair<Boolean, String?> {
        return try {
            val cpfQuery = db.collection("userInfo").whereEqualTo("cpf", cpf).get().await()
            if (!cpfQuery.isEmpty) {
                return Pair(false, "CPF_EXISTS")
            }
            if (email.endsWith("@fmveiculos.com")) {
                return Pair(false, "DOMAIN_NOT_ALLOWED")
            }
            auth.createUserWithEmailAndPassword(email, password).await()
            Pair(true, null)
        } catch (_: FirebaseAuthUserCollisionException) {
            Pair(false, "EMAIL_EXISTS")
        } catch (e: Exception) {
            Pair(false, "$e")
        }
    }

    suspend fun saveUserInfo(userInfo: UserInfoModel): Boolean {
        return try {
            db.collection("userInfo").document(userInfo.id).set(userInfo).await()
            true
        } catch (_: Exception) {
            false
        }
    }

    suspend fun getUserInfo(userId: String): UserInfoModel? {
        return try {
            val document = db.collection("userInfo").document(userId).get().await()
            document.toObject(UserInfoModel::class.java)
        } catch (_: Exception) {
            null
        }
    }

    fun checkLoggedUser(): Boolean {
        return auth.currentUser != null
    }

    fun getCurrentUserEmail(): String? {
        return auth.currentUser?.email
    }

    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    fun sendPasswordResetEmail(email: String): Boolean {
        return try {
            auth.sendPasswordResetEmail(email)
            true
        } catch (_: Exception) {
            false
        }
    }

    fun signOut() {
        auth.signOut()
    }
}