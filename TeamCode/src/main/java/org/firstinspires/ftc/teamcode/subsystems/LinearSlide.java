package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.commands.State;

public class LinearSlide {

    private DcMotor slide;
    private int slideTarget;

    public LinearSlide(HardwareMap hardwareMap) {

        slide = hardwareMap.dcMotor.get("slide");

        slide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slide.setTargetPosition(0);
        slide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slideTarget = 0;
        slide.setPower(2.9);

    }

    public void setPosition(State state){
        switch(state){
            case INTAKING:
                slideTarget = 0;
                break;
            case MID:
                slideTarget = 3000;
        }
    }

}
