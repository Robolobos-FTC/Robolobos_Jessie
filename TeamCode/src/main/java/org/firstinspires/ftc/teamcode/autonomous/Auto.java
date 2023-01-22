package org.firstinspires.ftc.teamcode.autonomous;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.subsystems.*;

@Config
public abstract class Auto extends LinearOpMode {

    Robot bot;

    SampleMecanumDrive drive;

    TrajectorySequence Wait;
    TrajectorySequence ScorePreload;

    @Override
    public void runOpMode() {

        bot = new Robot(hardwareMap, telemetry);
        drive = new SampleMecanumDrive(hardwareMap);

        build();

        waitForStart();

        execute();

    }

    public abstract void build();
    public void execute(){

    }

    public TrajectorySequence waitSequence(TrajectorySequence preceding, double time){
        return drive.trajectorySequenceBuilder(preceding.end())
                .waitSeconds(time)
                .build();
    }
}
