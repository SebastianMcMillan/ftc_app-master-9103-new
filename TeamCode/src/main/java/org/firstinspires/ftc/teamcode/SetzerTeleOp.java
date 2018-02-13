package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
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
    private DcMotor backLeft; //Port 0 Hub 1
    private DcMotor backRight; //Port 1 Hub 1
    private DcMotor frontLeft; //Port 2 Hub 1
    private DcMotor frontRight; //Port 3 Hub 1
    private Servo glyphLeft; //Port 0 Hub 1
    private Servo glyphRight; //Port 1 Hub 1
    private DcMotor glyphMotor; //Port 0 Hub 2

    @Override
    public void runOpMode() throws InterruptedException {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        frontLeft = hardwareMap.dcMotor.get("frontLeft");
        frontRight = hardwareMap.dcMotor.get("frontRight");
        backLeft = hardwareMap.dcMotor.get("backLeft");
        backRight = hardwareMap.dcMotor.get("backRight");

        glyphLeft = hardwareMap.servo.get("glyphLeft");
        glyphRight = hardwareMap.servo.get("glyphRight");

        glyphMotor = hardwareMap.dcMotor.get("glyphMotor");

        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();
        runtime.reset();

        glyphLeft.setPosition(0.7); // 0.7 is the open position for glyph servo Left
        glyphRight.setPosition(0.3); // 0.3 is the open position for glyph servo Right

        while (opModeIsActive()) {

            int position = 0;

            telemetry.addData("Encoder Position", position);

            if (gamepad1.left_stick_x != 0 || gamepad1.left_stick_y != 0) /*If the left stick is not neutral...*/ {
                double drive = -gamepad1.left_stick_y; //Set the Drive to the negative value of the y-axis value
                double turn = gamepad1.left_stick_x; //Set the turn to the value of the x-axis value

                double leftPower;
                double rightPower;
                leftPower = Range.clip(drive + turn, -1.0, 1.0); //fun math
                rightPower = Range.clip(drive - turn, -1.0, 1.0); //fun math 2

                frontLeft.setPower(leftPower * 1.5); //These are hopefully self-explanatory.
                frontRight.setPower(rightPower * 1.5);
                backLeft.setPower(leftPower * 1.5);
                backRight.setPower(rightPower * 1.5);
            }
            if (gamepad1.left_stick_x == 0 || gamepad1.left_stick_y == 0){

                frontLeft.setPower(0); //These are hopefully self-explanatory.
                frontRight.setPower(0);
                backLeft.setPower(0);
                backRight.setPower(0);

            }
            if (gamepad1.a){

                glyphLeft.setPosition(0.9);
                glyphRight.setPosition(0.1);
            }
            if (gamepad1.b){

                glyphLeft.setPosition(0.7);
                glyphRight.setPosition(0.3);
            }
            if (gamepad1.dpad_up && position > 10000 ){

                position = 10000;
                glyphMotor.setPower(0);
            }

            if(gamepad1.dpad_down && position < 0 ){

                glyphMotor.setPower(0);
                position = 0;
            }

            if(gamepad1.dpad_up && position < 10000){

                glyphMotor.setPower(1);
                position = position + 1;
            }

            if(gamepad1.dpad_down && position >= 0){

                glyphMotor.setPower(-1);
                position = position - 1;
            }


        }
    }
}
