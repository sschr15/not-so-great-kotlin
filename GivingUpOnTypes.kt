/* 
 * A collection of type-related snippets which abuse Kotlin's suppression capabilities.
 * 
 * A quick note on Kotlin's suppression capabilities:
 *  - If it is deprecation notice, it can be suppressed.
 *  - If it is a warning, it can be suppressed.
 *  - If it is a deprecation error, it can be suppressed.
 *  - If it is an error, it can be suppressed.
 *  - If it can be expressed as a string, it can be suppressed.
 */

@file:Suppress("DANGEROUS_CHARACTERS", "unused", "UNUSED_PARAMETER")

//region: The interesting stuff
/*
 * All required functions for these snippets are in Kotlin's stdlib, Java's libraries,
 * or declared at the bottom of this file.
 */

// Sample 1: Unsafe casting
/**
 * A function which takes in two arguments and acts as though they are both comparable,
 * even though the arguments have no type other than Any.
 */
fun `What's type safety?`(a: Any, b: Any) {
    // TYPE_MISMATCH is a compile-time error - types "must" be compatible. USELESS_CAST is to prevent an unrelated error.
    @Suppress("TYPE_MISMATCH", "USELESS_CAST")
    println(max(a, b) as Any)

    /*
     * Compilation result (roughly translated to Java):
     * 
     * void function(Object a, Object b) {
     *     System.out.println(max((Comparable) a, (Comparable) b));
     * }
     */
}

// Sample 2: throwing null
/**
 * A function which implements Java's rather questionable behavior of `throw` statements.
 */
fun `Null safety? Get that out of here!`() {
    // NULL_FOR_NONNULL_TYPE is a compile-time error - you can't pass null to non-null locations.
    @Suppress("NULL_FOR_NONNULL_TYPE")
    throw null

    /*
     * Compilation result (roughly translated to Java):
     * 
     * void function() {
     *     throw null;
     * }
     */
}

// Sample 3: implicit numeric conversions
/**
 * A function which gets an [Int] then passes it to a function requiring a [Double],
 * without explicitly casting it.
 */
fun `Kotlin has useful converter functions, so we will not use them`(i: Int) {
    // TYPE_MISMATCH is a compile-time error - numerical types "must" be explicitly cast.
    @Suppress("TYPE_MISMATCH")
    useDouble(i)

    /*
     * Compilation result (roughly translated to Java):
     * 
     * void function(int i) {
     *     useDouble((double) i);
     * }
     */
}

// Sample 4: a rather complex scenario
/**
 * A function which takes in some nullable subtype of [Number] and returns an interface instance
 * containg that number, or null if the argument is null.
 */
fun <T : Number> `Overriding sometimes goes wrong`(n: T?): NumberProvider? {
    return if (n == null) null else object : NumberProvider {
        // PROPERTY_TYPE_MISMATCH_ON_OVERRIDE is a compile-time error - Kotlin isn't sure if the overridden property is nullable or not.
        @Suppress("PROPERTY_TYPE_MISMATCH_ON_OVERRIDE") // The strange fix: add this annotation.
        override val number = n // The simple fix: explicitly declare the type of the property.
    }

    /*
     * Compilation result (roughly translated to Java):
     * 
     * NumberProvider function(Number n) {
     *     if (n == null) {
     *         return null;
     *     } else {
     *         return new NumberProvider() {
     *             @Override
     *             public Number getNumber() {
     *                 return n;
     *             }
     *         };
     *     }
     * }
     */
}

//endregion

//region: Base functions

fun <T : Comparable<T>> max(a: T, b: T): T = if (a > b) a else b
fun useDouble(d: Double) {}

interface NumberProvider {
    val number: Number
}

//endregion
