package m.a.compilot.navigation.result

import java.io.Serializable

/**
 * This interface provides methods for setting various types of data to be returned as navigation results.
 */
interface Navigationhandler {

    /**
     * Sets a string value associated with the specified key.
     *
     * @param key The key with which the specified value is to be associated.
     * @param value The value to be associated with the specified key.
     */
    fun setString(key: String, value: String)

    /**
     * Sets a key for the navigation result.
     *
     * @param key The key to be set.
     */
    fun setKey(key: String)

    /**
     * Sets a boolean value associated with the specified key.
     *
     * @param key The key with which the specified value is to be associated.
     * @param value The value to be associated with the specified key.
     */
    fun setBoolean(key: String, value: Boolean)

    /**
     * Sets an integer value associated with the specified key.
     *
     * @param key The key with which the specified value is to be associated.
     * @param value The value to be associated with the specified key.
     */
    fun setInt(key: String, value: Int)

    /**
     * Sets a serializable value associated with the specified key.
     *
     * @param key The key with which the specified value is to be associated.
     * @param value The serializable value to be associated with the specified key.
     * @param T The type of the serializable value.
     */
    fun <T : Serializable> setSerializable(key: String, value: T)
}
