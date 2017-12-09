package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Sebastian on 11/10/2017.
 */

@TeleOp(name = "Ultima TeleOp -- New Control Test", group = "Linear OpMode")

public class UltimaTeleOpNewControlTest extends LinearOpMode {

    //Declare OpMode Members
    private ElapsedTime runtime = new ElapsedTime();

    //Declare Motor Controllers
    private DcMotorController redMage;
    private DcMotorController dalek;
    private DcMotorController andKnuckles;

    //Declare Motors
    private DcMotor backLeftMotor; //Port 1 On Controller dalek
    private DcMotor backRightMotor; //Port 2 On Controller dalek
    private DcMotor frontLeftMotor; //Port 1 On Controller redMage
    private DcMotor frontRightMotor; //Port 2 On Controller redMage
    private DcMotor glyphArmMotor; //Port 1 on Controller andKnuckles

    //Declare Servo Motor Controllers
    private ServoController sonicScrewdriver;

    //Declare Servo Motors
    private Servo leftServo; //Port 1 on Controller sonicScrewdriver
    private Servo rightServo; //Port 2 on Controller sonicScrewdriver
    private Servo backServo; //Port 2 On Controller sonicScrewdriver

    private int glyphManStatus = 0; //Set to 0; 0 is open, 1 is closed.
    private int glyphArmPosition = 1; //Set Arm Position to 0.
    @Override
    public void runOpMode() throws InterruptedException {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        //HW Map Motors on Controller redMage
        frontLeftMotor = hardwareMap.dcMotor.get("frontLeftMotor");
        frontRightMotor = hardwareMap.dcMotor.get("frontRightMotor");

        //HW Map Motors on Controller dalek
        backLeftMotor = hardwareMap.dcMotor.get("backLeftMotor");
        backRightMotor = hardwareMap.dcMotor.get("backRightMotor");

        //HW Map Motors on Controller andKnuckles
        glyphArmMotor = hardwareMap.dcMotor.get("glyphArmMotor");

        //HW Map Servos on Controller sonicScrewdriver
        leftServo = hardwareMap.servo.get("leftServo");
        rightServo = hardwareMap.servo.get("rightServo");
        backServo = hardwareMap.servo.get("backServo");

        //Reverse Direction of The Left Motors
        frontLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        //Reverse Direction of The Glyph Arm Motor
        glyphArmMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        //Open Glyph Arm to match Current Status
        leftServo.setPosition(0.0);
        rightServo.setPosition(1.0);

        //Move Back Servo Arm Thing to Inside the bot
        backServo.setPosition(0.0);

        waitForStart();
        runtime.reset();

        //Run until end of match (when driver presses stop)
        while (opModeIsActive()) {
            double leftPower;
            double rightPower;
            double strafePower = gamepad1.left_stick_x;
            double strafeTurn = gamepad1.left_stick_y/2;
            double turn = gamepad1.right_stick_x;

            //Moving Forward/Backwards (And Drive Turn)
            if (gamepad1.left_stick_x == 0 && gamepad1.left_stick_y != 0) {
                double drive = -gamepad1.left_stick_y;
                double driveTurn = gamepad1.right_stick_x;

                leftPower = Range.clip(drive + driveTurn, -1.0, 1.0);
                rightPower = Range.clip(drive - driveTurn, -1.0, 1.0);

                if (gamepad1.x) {
                    leftPower = leftPower/4;
                    rightPower = rightPower/4;
                }
                frontLeftMotor.setPower(leftPower);
                frontRightMotor.setPower(rightPower);
                backLeftMotor.setPower(leftPower);
                backRightMotor.setPower(rightPower);
            }

            //Diagonal Movement
            if (gamepad1.left_stick_x != 0 && gamepad1.left_stick_y != 0) {
                if (gamepad1.x) {
                    strafePower = strafePower/4;
                    strafeTurn = strafeTurn/4;
                }
                if (gamepad1.left_stick_x < 0.0) {
                    frontLeftMotor.setPower(strafePower);
                    backRightMotor.setPower(strafePower);
                    frontRightMotor.setPower(strafeTurn);
                    backLeftMotor.setPower(strafeTurn);
                }
                if (gamepad1.left_stick_x > 0.0) {
                    frontRightMotor.setPower(strafePower);
                    backLeftMotor.setPower(strafePower);
                    frontLeftMotor.setPower(strafeTurn);
                    backRightMotor.setPower(strafeTurn);
                }
            }

            //Point-Turn Input
            if (gamepad1.right_stick_x != 0 && gamepad1.left_stick_y == 0) {
                if (gamepad1.x) {
                    turn = turn/4;
                }
                    frontLeftMotor.setPower(turn);
                    frontRightMotor.setPower(-turn);
                    backLeftMotor.setPower(turn);
                    backRightMotor.setPower(-turn);
            }

            //If D-Pad Up is Pressed, Move the Glyph Manipulator Up
            if (gamepad1.dpad_up && glyphArmPosition < 3600) {
                glyphArmPosition = glyphArmPosition +30;
                glyphArmMotor.setTargetPosition(glyphArmPosition);
            }

            //If D-Pad Down is Pressed, Move the Glyph Manipulator Down
            if (gamepad1.dpad_down && glyphArmPosition > 0) {
                glyphArmPosition = glyphArmPosition - 30;
                glyphArmMotor.setTargetPosition(glyphArmPosition);
                if (glyphArmPosition <= 0) {
                    glyphArmPosition = 1;
                }
            }

            //If A, Close the Glyph manipulator
            if (gamepad1.a) {
                leftServo.setPosition(0.5);
                rightServo.setPosition(0.5);
                glyphManStatus = 1;
            }

            //If B is pressed, Open the Glyph manipulator
            if (gamepad1.b) {
                leftServo.setPosition(0.0);
                rightServo.setPosition(1.0);
                glyphManStatus = 0;
            }

            //If The Sticks are neutral, lets stop the motors.
            if (gamepad1.left_stick_x == 0 && gamepad1.left_stick_y == 0 && gamepad1.right_stick_x == 0)
            {
                frontLeftMotor.setPower(0.0);
                frontRightMotor.setPower(0.0);
                backLeftMotor.setPower(0.0);
                backRightMotor.setPower(0.0);
            }
            telemetry.addData("Status", "Running");
            telemetry.addData("Glyph Arm Motor Encoder Reading:", glyphArmPosition);
            if (glyphManStatus == 0) {
                telemetry.addData("Glyph Manipulator Status:", "Open");
            }
            if (glyphManStatus ==1) {
                telemetry.addData("Glyph Manipulator Status:", "Close");
            }
            telemetry.update();
            idle();
        }
    }
}
