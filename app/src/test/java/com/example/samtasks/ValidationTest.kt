package com.example.samtasks

import com.example.samtasks.utils.ValidationResult
import com.example.samtasks.utils.validateAsEmail
import com.example.samtasks.utils.validateAsName
import com.example.samtasks.utils.validateAsPassword
import org.junit.Test
import org.junit.Assert.*

class ValidationTest {

    @Test
    fun `empty text validated as email returns empty field validation error`(){
        val emptyText= ""
        val result = emptyText.validateAsEmail()
        assertEquals(ValidationResult.EMPTY,result)
    }

    @Test
    fun `empty name is invalid`(){
        // Given an empty name text
        val empty = ""

        // When validating this text as name
        val result = empty.validateAsName()

        // Then result must be empty
        assertEquals(ValidationResult.EMPTY,result)
    }

    @Test
    fun `not empty text passes test as a name`(){
        // Given a text that's not empty
        val name = "person name"

        // When validating as name
        val result = name.validateAsName()

        // Then result must be valid
        assertEquals(ValidationResult.VALID,result)
    }

    @Test
    fun `short password does not pass the test`(){
        // Given a password less than 8 chars
        val pw = "1234567"

        // When validating as password
        val result = pw.validateAsPassword()

        // result  must be password too short
        assertEquals(ValidationResult.PASSWORD_TOO_SHORT,result)
    }

    @Test
    fun `long password passes the test`(){
        // Given that password is long enough
        val password = "validPassword"

        // When validating as password
        val result = password.validateAsPassword()

        // Then it must be valid
        assertEquals(ValidationResult.VALID,result)
    }
}