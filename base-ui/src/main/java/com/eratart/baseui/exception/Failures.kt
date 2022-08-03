package com.eratart.baseui.exception

import androidx.annotation.StringRes
import com.eratart.baseui.R

abstract class BaseFailure(@StringRes val errorTextRes: Int)

class DefaultFailure : BaseFailure(R.string.failure_general)
class NoNetworkFailure : BaseFailure(R.string.failure_general)
class UnauthorizedFailure : BaseFailure(R.string.failure_general)