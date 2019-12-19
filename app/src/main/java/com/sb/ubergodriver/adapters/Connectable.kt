/*
 *  Created By Sudesh Bishnoi
 */

package com.sb.ubergodriver.adapters
import io.reactivex.disposables.Disposable

interface Connectable {
    fun connect(): Disposable
}
