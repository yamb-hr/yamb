package com.tejko.yamb.exceptions.custom;

import com.tejko.yamb.domain.constants.MessageConstants;

public class DiceRollRequiredException extends IllegalStateException {

    public DiceRollRequiredException() {
        super(MessageConstants.ERROR_DICE_ROLL_REQUIRED);
    }

}
