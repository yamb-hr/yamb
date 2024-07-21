package com.tejko.yamb.domain.exceptions;

import com.tejko.yamb.domain.constants.MessageConstants;

public class DiceRollRequiredException extends IllegalStateException {

    public DiceRollRequiredException() {
        super(MessageConstants.ERROR_DICE_ROLL_REQUIRED);
    }

}
