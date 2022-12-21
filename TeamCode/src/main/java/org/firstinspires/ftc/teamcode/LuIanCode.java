/*
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name="Lu Ian Code")

public class LuIanCode extends LinearOpMode{
    //private double adj = 0.77;
    //used when the robot is not balanced. Forces one side of motors to spin faster so the robot doesn't rotate (unfortunate downside of mecanum wheels)
    private DcMotorEx frontRight, frontLeft, backRight, backLeft, arm; //Instantiating all DC motorex, DO NOT USE DCMotor, as you won't be able to use encoders
    private ElapsedTime runtime = new ElapsedTime(); //variable to view runtime
    private double right = 300, left = -300;

    @Override

    public void runOpMode() throws InterruptedException {
        frontRight = hardwareMap.get(DcMotorEx.class, "front right");
        frontLeft = hardwareMap.get(DcMotorEx.class, "front left");
        backRight = hardwareMap.get(DcMotorEx.class, "back right");
        backLeft = hardwareMap.get(DcMotorEx.class, "back left");


        //arm = hardwareMap.get(DcMotorEx.class, "slide"); //2 (motors on expansion hub, labelled correctly this time)
        //rightClaw = hardwareMap.get(DcMotorEx.class, "rightClaw");
        //leftClaw = hardwareMap.get(DcMotorEx.class, "leftClaw");

        arm.hardwareMap.get(DcMotorEx.class, "extension");
        slide.hardwareMap.get(CRServo.class, "slide");
        rightClaw.get(CRServo.class, "rightClaw");
        leftClaw.get(Servo.class, "leftClaw");


         //set motors to run based on velocity rather than power (more consistent if the battery is differently charged)
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);



         //arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER); //Lets the encoder know the current position is 0
         //arm.setTargetPosition(0); //Needs a target position, so is set to 0, or initialization position
         //arm.setPower(0.5); //Sets arm to move at half power. Too high and the gears will break, too low and it will be slow
         //arm.setMode(DcMotor.RunMode.RUN_TO_POSITION); //Use mode run to position

      //   runtime.reset();


        while(opModeIsActive()) {

         INSTRUCTIONS/TO-DO LIST BELOW!!!!!!!!!!
        % DO THE BELOW FIRST %
        [create a method for going 1 tile, so we can j reuse it]

        [create a method for turning right 90 degrees & turning left 90 degrees]
        [create a method for picking up & dropping cones]


        To-Code Physical Directions:
        go straight for 3 tiles
        right turn 90d
        forward 1 tile
        call slider method from BonnieBrain to raise to highest pole (lvl 3 junction)


        waitForStart();
        */
        /*
        luForward(3, 1300);
        luRight(4, 800);
        luLeft(4,800);

    }

    // super cool code that makes the bot go forward by powering all motors to go straight
    private void luForward(double mult, int ms){
        frontRight.setVelocity(mult * right);
        frontLeft.setVelocity(mult * left);
        backRight.setVelocity(mult * right);
        backLeft.setVelocity(mult * left);
        runFor(ms); //runs for this amount of time
        // Mecanum forward (+ means forward, - means backwards)
    }

    // super cool code that makes the bot turn right by reversing the front right and back right wheels
    private void luRight(double mult, int ms) {
        frontRight.setVelocity(mult * -right);
        frontLeft.setVelocity(mult * left);
        backRight.setVelocity(mult * -right);
        backLeft.setVelocity(mult * left);
        runFor(ms); //runs for this amount of time
    }

    // super cool code that makes the bot turn left by reversing the front right and back left wheels
    private void luLeft(double mult, int ms) {
        frontRight.setVelocity(mult * -right);
        frontLeft.setVelocity(mult * left);
        backRight.setVelocity(mult * right);
        backLeft.setVelocity(mult * -left);
        runFor(ms); //runs for this amount of time

    }
    // insane method that sets all power to wheel motors at 0
    private void luStopRobot(){
        frontRight.setVelocity(0);
        frontLeft.setVelocity(0);
        backRight.setVelocity(0);
        backLeft.setVelocity(0);
    }
    private void luRunFor(int ms){
        //sleeps for given time, so the program can run. FTC sleep means keep doing what you are doing, not stop everything
        sleep(ms);
        stopRobot();
        sleep(1000);
    }

}
*/
