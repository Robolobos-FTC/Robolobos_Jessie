## Welcome!

This repository contains all of the code for the FTC team 14363 Robolobos.
Op-mode code and autonomous code is found in the /TeamCode/src/main/java/org/firstinspires/ftc/teamcode folder

Thank you to FTC. Most of the code is written by them.
package org.firstinspires.ftc.teamcode;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;


@TeleOp (name = "ILUVBALLZZ")

public class ILUVBALLZZ extends LinearOpMode{
@Override
public void runOpMode() throws InterruptedException {
// Declare our motors
// Make sure your ID's match your configuration
DcMotor motorFrontLeft = hardwareMap.dcMotor.get("motorFrontLeft");
DcMotor motorBackLeft = hardwareMap.dcMotor.get("motorBackLeft");
DcMotor motorFrontRight = hardwareMap.dcMotor.get("motorFrontRight");
DcMotor motorBackRight = hardwareMap.dcMotor.get("motorBackRight");

        // Reverse the right side motors
        // Reverse left motors if you are using NeveRests
        motorFrontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        motorBackLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        IMU imu = hardwareMap.get(IMU.class, "imu");
        // Adjust the orientation parameters to match your robot
        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.RIGHT));
        // Without this, the REV Hub's orientation is assumed to be logo up / USB forward
        imu.initialize(parameters);
        imu.resetYaw();
        double botHeading = -imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
        waitForStart();
        if (isStopRequested()) return;

        while (opModeIsActive()) {
            botHeading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
            //REVERSE THIS BOZOOOOOOOOOOOOO
            double y = -gamepad1.left_stick_y; // Remember, this is reversed!
            double x = gamepad1.left_stick_x * 1.1; // Counteract imperfect strafing
            double rx = gamepad1.right_stick_x;
        //if (gamepad1.options) {
        //    imu.resetYaw();
        //}

        // Rotate the movement direction counter to the bot's rotation
        double rotX = x * Math.cos(-botHeading) - y * Math.sin(-botHeading);
        double rotY = x * Math.sin(-botHeading) + y * Math.cos(-botHeading);

        // Denominator is the largest motor power (absolute value) or 1
        // This ensures all the powers maintain the same ratio, but only when
        // at least one is out of the range [-1, 1]
        double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(rx), 1);
        double frontLeftPower = (rotY + rotX + rx) / denominator;
        double backLeftPower = (rotY - rotX + rx) / denominator;
        double frontRightPower = (rotY - rotX - rx) / denominator;
        double backRightPower = (rotY + rotX - rx) / denominator;

        motorFrontLeft.setPower(frontLeftPower * .75);
        motorBackLeft.setPower(backLeftPower * .75);
        motorFrontRight.setPower(frontRightPower * .75);
        motorBackRight.setPower(backRightPower * .75);
        telemetry.addData("Head: ", Math.toDegrees(botHeading));
        telemetry.update();
        }
    }
}




