package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by Sebastian McMillan on 2/6/18.
 */

@TeleOp(name= "Setzer TeleOp", group = "Linear OpMode")

public class SetzerTeleOp extends LinearOpMode {
    //Initializing The Robot

    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() throws InterruptedException {
        telemetry.addData("Status", "Initialized");
        telemetry.update();
    }
}
