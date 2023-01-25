package org.firstinspires.ftc.teamcode.autonomous;

import static java.lang.Math.toRadians;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.commands.State;

@Autonomous(name="John Auton")

public class JohnAuton extends Auto {

    private double waitAtScore = 0.2;
    private double waitAtStorage;

    public static Pose2d INIT = new Pose2d(-34, -65, toRadians(180));

    public void build(){

        drive.setPoseEstimate(INIT);
        bot.claw.close();

        ScorePreload = drive.trajectorySequenceBuilder(INIT)
                .addTemporalMarker(0.4, () -> bot.setPosition(State.AUTOMID))
                .back(20)
                .lineToConstantHeading(new Vector2d(-13.5, -15))
                .build();

        WaitAtScore1 = waitSequence(ScorePreload, waitAtScore);

        ScoreToStorage1 = drive.trajectorySequenceBuilder(ScorePreload.end())
                .addTemporalMarker(1, () -> bot.slide.setPosition(State.CONE5))
                .strafeRight(10)
                .lineToConstantHeading(new Vector2d(-61, -2))

                .build();

        StorageToScore2 = drive.trajectorySequenceBuilder(ScoreToStorage1.end())

                .addTemporalMarker(2, () -> bot.setPosition(State.AUTOMID))
                // remember, toRadians will set the orientation of the robot using the unit circle
                // 0 degrees is directly right of the field
                .lineToLinearHeading(new Pose2d( -30, -2, toRadians(345)))


                .build();
    }
}
