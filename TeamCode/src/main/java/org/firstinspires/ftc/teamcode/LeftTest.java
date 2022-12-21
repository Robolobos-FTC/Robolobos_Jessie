package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.*;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.hardware.bosch.BNO055IMU;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.*;

@TeleOp(name="LeftTest", group="DriveModes")
public class LeftTest extends OpMode {
    /*
    Max Velocity:
    145.1 Ticks per Revolution
    1150 RPM
    .setVelocity() is ticks per second
    1150/60 = 19.1 RPS
    19.1 RPS * 145.1 TPR = 2781.083 TPS
    */
    private double maxV = 1150.0/60.0*145.1 * 0.8; //max rpm 80%
    private DcMotorEx TL, BL, TR, BR;
    private double x, y, rx, r, theta, curDeg, rot, TRv, BRv, TLv, BLv, pivot;
    private int curTick, inverse;
    private boolean toInverse;
    private final int A = 1000;
    private final int B = 1000;
    private final double MOTOR_TICKS = 145.1;
    private final double RATIO = 106.0/13 * 18.0/14; // 106,32,13,18,14 106/13 * 18/14 = 10.4835
    private ElapsedTime runtime;
    private BNO055IMU imu;

    @Override
    public void init() {
        telemetry.addData("Status", "Initializing");
        runtime = new ElapsedTime();

        //top and bottom spin opposite by default
        //TL = hardwareMap.get(DcMotorEx.class, "TL"); //1
        //BL = hardwareMap.get(DcMotorEx.class, "BL"); //1
        TR = hardwareMap.get(DcMotorEx.class, "TR"); //1
        BR = hardwareMap.get(DcMotorEx.class, "BR"); //1

        TR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        BR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        TR.setDirection(DcMotorSimple.Direction.REVERSE);
        BR.setDirection(DcMotorSimple.Direction.REVERSE);
        curDeg = 0;
        theta = 0;
        pivot = 0;
        toInverse = false;

        imu = hardwareMap.get(BNO055IMU.class, "imu");
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        imu.initialize(parameters);

        telemetry.addData("Status", "Initilized");

    }
    @Override
    public void init_loop() {
    }
    @Override
    public void start() {
        runtime.reset();

    }
    @Override
    public void loop() {
        x = Math.abs(gamepad1.left_stick_x) > 0.01 ? gamepad1.left_stick_x : 0; //sideways movement
        y = Math.abs(gamepad1.left_stick_y) > 0.01 ? gamepad1.left_stick_y : 0; //forward movement
        rx = gamepad1.right_stick_x; //rotational movement

        /*
        TICK CALCULATION
            - Finds the difference/2 of the encoder values
            - this "cancels out" opposite movement, or, in other words, lateral movement,
              accounting for ONLY wheel pivot
        */
        curTick = (TR.getCurrentPosition() - BR.getCurrentPosition()) / 2;

        /*
        ROTATION CALCULATION
            - Convert ticks to rotation in float form
            - Divide the ticks calculated previously by the ticks per rotation of the motor
            - Divide this further by the amount of rotations the motor needs to fully spin
              a ring gear using gear ratios
        */
        rot = curTick / MOTOR_TICKS / RATIO;

        /*
        DEGREE CALCULATION
            - Convert the rotation percent to degrees
            - Isolate the decimal, and multiply it by 360 for a full scale
        */
        curDeg = Math.signum(rot)*(Math.abs(rot) - Math.floor(Math.abs(rot))) * 360.0;

        r = Math.sqrt(x*x + y*y);
        if(x != 0 || y != 0) theta = Math.atan2(x, y) * 180 / Math.PI - imu.getAngularOrientation().firstAngle; //Degree of lateral movement stick, forward is 0, right is 90

        pivot = Math.sin((curDeg - theta) * Math.PI / 180) * A;
        double travel = Math.abs((curDeg+360)%360-(theta+360)%360);
        toInverse = Math.min(travel, 360.0 - travel) >= 90.0;
        if(!toInverse){
            inverse = 1;
        }else{
            inverse = -1;
            pivot *= -1;
        }

        //TLv = pivot - 1/2 * (r + rx) * B;
        //BLv = pivot + 1/2 * (r + rx) * B;
        TRv = (-pivot - 0.5
                *
                (r-rx) * B * inverse);
        BRv = (pivot - 0.5
                *
                (r-rx) * B * inverse);

        //double high = Math.max(Math.abs(TRv), Math.abs(BRv));
        //TRv /= Math.max(maxV, high);
        //BRv /= Math.max(maxV, high);

        TR.setVelocity(TRv);
        BR.setVelocity(BRv);

        telemetry.addData("Status", "Run Time: " + runtime.toString());
        telemetry.addData("Joystick: ", Math.floor(theta * 100) / 100);
        telemetry.addData("Wheels:   ", Math.floor(curDeg * 100) / 100);
        telemetry.addData("TR V:     ", Math.floor(TR.getVelocity() * 100) / 100);
        telemetry.addData("BR V:     ", Math.floor(BR.getVelocity() * 100) / 100);
        telemetry.addData("ADiff:    ", Math.floor(Math.min(travel, 360.0 - travel) * 100) / 100);
        telemetry.addData("Heading:  ", imu.getAngularOrientation().firstAngle);
    }
    @Override
    public void stop() {
    }
}