package com.marina.ruiz.globetrotting.domain

import com.marina.ruiz.globetrotting.data.network.firebase.AuthService
import javax.inject.Inject

class LogoutUseCase @Inject constructor(private val authSvc: AuthService) {
    operator fun invoke() = authSvc.logout()
}