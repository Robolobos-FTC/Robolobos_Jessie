package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.CRServoImplEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Extension {

    private CRServoImplEx extension;

    public Extension (HardwareMap hardwareMap) {

        extension = hardwareMap.get(CRServoImplEx.class, "extension");

    }
}
