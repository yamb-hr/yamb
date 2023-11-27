package com.tejko.yamb.exceptions;

import com.tejko.yamb.constants.MessageConstants;

public class DiceRollRequiredException extends IllegalStateException {

    public DiceRollRequiredException() {
        super(MessageConstants.ERROR_DICE_ROLL_REQUIRED);
    }

}
