package org.firstinspires.ftc.teamcode;

import static java.lang.Math.toRadians;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServoImplEx;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

@Autonomous(name="John Auton")

public class JohnAuton extends LinearOpMode {

    @Override
    public void runOpMode() {
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        //Slide Motor
        DcMotor slide = hardwareMap.dcMotor.get("slide");
        // Claw extension
        CRServoImplEx extension = hardwareMap.get(CRServoImplEx.class, "extension");
        // Claw Motors
        Servo rightClaw = hardwareMap.servo.get("rightClaw");
        Servo leftClaw = hardwareMap.servo.get("leftClaw");

        // Slide Presets
        slide.setMode(DcMotor.RunMode.RUN_USING_ENCODERS);
        slide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slide.setTargetPosition(0);
        slide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        int slideTarget = 0;
        waitForStart();
        slide.setPower(2.9);

        Pose2d startPose = new Pose2d(-34, -65, toRadians(-90));

        drive.setPoseEstimate(startPose);

        Trajectory traj1 = drive.trajectoryBuilder(startPose)
                .back(17)
                .build();

        Trajectory traj2 = drive.trajectoryBuilder(traj1.end())
                .strafeRight(48)
                .addTemporalMarker(2, () -> {extension.setPower(0);})
                .build();

        Trajectory traj3 = drive.trajectoryBuilder(traj2.end())
                .strafeRight(20)
                .addTemporalMarker(.5, () -> {
                    //Close Claw
                    leftClaw.setPosition(0.1);
                    rightClaw.setPosition(1);})
                .build();

        Trajectory traj4 = drive.trajectoryBuilder(traj3.end())
                .forward(55)
                .addTemporalMarker(2, () -> {extension.setPower(0);})
                .build();

        waitForStart();

        if(isStopRequested()) return;

        //
        // ROBOT AUTONOMOUS MOVEMENT ORDER
        //

        //Close Claw
        leftClaw.setPosition(0.1);
        rightClaw.setPosition(1);

        //Move Back
        drive.followTrajectory(traj1);

        //Slide Up
        slide.setTargetPosition(2800);

        //Extension Up
        extension.setPower(-1);

        //Strafe Right and Extension Stop
        drive.followTrajectory(traj2);

        //Slide Down
        slide.setTargetPosition(0);
        sleep(500);

        //Open Claw
        leftClaw.setPosition(0.4);
        rightClaw.setPosition(0.8);

        //Move right
        drive.followTrajectory(traj3);

        //Extension Down
        extension.setPower(1);

        //Move To Stack
        drive.followTrajectory(traj4);

    }
}
