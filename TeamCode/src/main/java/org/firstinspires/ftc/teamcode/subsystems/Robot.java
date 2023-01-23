package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.commands.State;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Robot {

    public Claw claw;
    public LinearSlide slide;
    public Extension extension;

    public Robot(HardwareMap hardwareMap, Telemetry telemetry){

        claw = new Claw(hardwareMap);
        slide = new LinearSlide(hardwareMap);
        extension = new Extension(hardwareMap);

    }

    public void setPosition(State state){
        claw.setPosition(state);
        slide.setPosition(state);
    }

}
