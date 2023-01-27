package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.commands.State;

public class LinearSlide {

    private DcMotor slide;
    private int slideTarget;
    private int HIGH = 4400, MID = 3000, LOW = 1350, INTAKE = 0;
    private int AUTOMID = 3600;
    private int CONE5 = 840;
    private int CONE4 = 800;
    public LinearSlide(HardwareMap hardwareMap) {

        slide = hardwareMap.dcMotor.get("slide");

        slide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slide.setTargetPosition(0);
        slide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slide.setPower(2.9);

    }

    public void setPosition(State state){
        switch(state){
            case INTAKING:
                setTarget(INTAKE);
                break;
            case MID:
                setTarget(MID);
                break;
            case AUTOMID:
                setTarget(AUTOMID);
                break;

            case CONE5:
                setTarget(CONE5);
                break;
            case CONE4:
                setTarget(CONE4);
        }
    }

    public void setTarget(int position){
        slide.setTargetPosition(position);
    }

}
