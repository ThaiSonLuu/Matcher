package com.sanryoo.matcher.features.domain.usecase

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class SignUpUnitTest {

    @Test
    fun usernameContainUppercase_returnFalse() {
        val result = isValidUsername("abC213213")
        assertThat(result).isFalse()
    }

    @Test
    fun usernameContainSpace_returnFalse() {
        val result = isValidUsername("abc 213213")
        assertThat(result).isFalse()
    }

    @Test
    fun usernameContainSpecialCharacter_returnFalse() {
        val result = isValidUsername("abc*_213213")
        assertThat(result).isFalse()
    }

    @Test
    fun usernameStartWithANumber_returnFalse() {
        val result = isValidUsername("1abc213123")
        assertThat(result).isFalse()
    }

    @Test
    fun usernameValid_returnTrue() {
        val result = isValidUsername("abc21321312")
        assertThat(result).isTrue()
    }


    @Test
    fun passwordNotContainNumber_returnFalse() {
        val result = checkPassword("ADSAFabcdefghti.")
        assertThat(result).isFalse()
    }

    @Test
    fun passwordNotContainLowercase_returnFalse() {
        val result = checkPassword("AFGSADBNBCV1123123.")
        assertThat(result).isFalse()
    }

    @Test
    fun passwordNotContainUppercase_returnFalse() {
        val result = checkPassword("abcdefghti213123123.")
        assertThat(result).isFalse()
    }

    @Test
    fun passwordNotContainSpecialNumber_returnFalse() {
        val result = checkPassword("BAJBADSabcdefghti21351253")
        assertThat(result).isFalse()
    }

    @Test
    fun passwordValid_returnTrue() {
        val result = checkPassword("BAJBADSabcdefghti21351253.")
        assertThat(result).isTrue()
    }

}