package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.commands.State;

public class Claw {

    private Servo rightClaw;
    private Servo leftClaw;

    public Claw(HardwareMap hardwareMap){

        rightClaw = hardwareMap.servo.get("rightClaw");
        leftClaw = hardwareMap.servo.get("leftClaw");

    }

    public void setPosition(State state){
        switch(state){
            case INTAKING:
                open();
                break;
            default:
                close();
                break;
        }
    }

    public void close(){
        leftClaw.setPosition(0.1);
        rightClaw.setPosition(1);
    }

    public void open(){
        leftClaw.setPosition(0.4);
        rightClaw.setPosition(0.8);
    }
}
