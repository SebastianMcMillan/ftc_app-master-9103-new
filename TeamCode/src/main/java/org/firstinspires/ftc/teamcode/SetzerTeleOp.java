package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Sebastian McMillan on 2/6/18.
 */

@TeleOp(name= "Setzer TeleOp", group = "Linear OpMode")

public class SetzerTeleOp extends LinearOpMode {
    //Initializing The Robot

    private ElapsedTime runtime = new ElapsedTime();

    //Declare Motors
    private DcMotor backLeft; //Port 0
    private DcMotor backRight; //Port 1
    private DcMotor frontLeft; //Port 2
    private DcMotor frontRight; //Port 3

    @Override
    public void runOpMode() throws InterruptedException {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        frontLeft = hardwareMap.dcMotor.get("frontLeft");
        frontRight = hardwareMap.dcMotor.get("frontRight");
        backLeft = hardwareMap.dcMotor.get("backLeft");
        backRight = hardwareMap.dcMotor.get("backRight");


        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {

            if (gamepad1.left_stick_x != 0 || gamepad1.left_stick_y != 0) /*If the left stick is not neutral...*/ {
                double drive = -gamepad1.left_stick_y; //Set the Drive to the negative value of the y-axis value
                double turn = gamepad1.left_stick_x; //Set the turn to the value of the x-axis value

                double leftPower;
                double rightPower;
                leftPower = Range.clip(drive + turn, -1.0, 1.0); //fun math
                rightPower = Range.clip(drive - turn, -1.0, 1.0); //fun math 2

                frontLeft.setPower(leftPower / 3); //These are hopefully self-explanatory.
                frontRight.setPower(rightPower / 3);
                backLeft.setPower(leftPower / 3);
                backRight.setPower(rightPower / 3);
            if (gamepad1.left_stick_x == 0 || gamepad1.left_stick_y == 0){

                frontLeft.setPower(0); //These are hopefully self-explanatory.
                frontRight.setPower(0);
                backLeft.setPower(0);
                backRight.setPower(0);

            }
            }
        }
    }
}
