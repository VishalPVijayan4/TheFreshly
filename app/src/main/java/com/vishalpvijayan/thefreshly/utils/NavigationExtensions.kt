package com.vishalpvijayan.thefreshly.utils

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.NavDirections

/**
 * Navigates only when the requested action/destination is valid for the current destination.
 * This makes rapid taps and delayed asynchronous callbacks idempotent instead of throwing an
 * IllegalArgumentException after the first navigation has already changed the back stack.
 */
fun NavController.navigateSafely(@IdRes actionOrDestinationId: Int, args: Bundle? = null): Boolean {
    val current = currentDestination ?: return false
    val action = current.getAction(actionOrDestinationId)
    val destinationId = action?.destinationId ?: actionOrDestinationId

    if (current.id == destinationId) return true
    if (action == null && graph.findNode(actionOrDestinationId) == null) return false

    return runCatching {
        navigate(actionOrDestinationId, args)
    }.isSuccess
}

fun NavController.navigateSafely(directions: NavDirections): Boolean {
    val current = currentDestination ?: return false
    val action = current.getAction(directions.actionId) ?: return false
    if (current.id == action.destinationId) return true

    return runCatching { navigate(directions) }.isSuccess
}
