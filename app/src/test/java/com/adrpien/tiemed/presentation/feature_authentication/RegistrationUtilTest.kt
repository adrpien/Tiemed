package com.adrpien.tiemed.presentation.feature_authentication

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class RegistrationUtilTest {

    @Test
    fun `empty userName return false`() {
        val result = RegistrationUtil.validateRegistrationInput(
            userName = "",
            password = "1222",
            confirmPassword = "1222"
        )
        assertThat(result).isFalse()
     }

    @Test
    fun `valid userName and correctly repeated password returns true`() {
        val result = RegistrationUtil.validateRegistrationInput(
            userName = "Eryk",
            password = "1222",
            confirmPassword = "1222"
        )
        assertThat(result).isTrue()
    }

    @Test
    fun `incorrectly repeated password returns false`() {
        val result = RegistrationUtil.validateRegistrationInput(
            userName = "Eryk",
            password = "1222",
            confirmPassword = "1223"
        )
        assertThat(result).isFalse()
    }

    @Test
    fun `userName is already taken returns false`() {
        val result = RegistrationUtil.validateRegistrationInput(
            userName = "Michał",
            password = "1222",
            confirmPassword = "1222"
        )
        assertThat(result).isFalse()
    }

    @Test
    fun `empty password return false`() {
        val result = RegistrationUtil.validateRegistrationInput(
            userName = "Klara",
            password = "",
            confirmPassword = ""
        )
        assertThat(result).isFalse()
    }
}