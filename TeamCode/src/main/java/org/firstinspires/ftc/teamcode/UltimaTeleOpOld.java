package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
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

@TeleOp(name = "Project Ultima TeleOp -- OBSOLETE! IF YOU SEE THIS, THIS IS A BUG! RUN ULTIMA TELEOP INSTEAD", group = "Linear OpMode")
@Disabled
public class UltimaTeleOpOld extends LinearOpMode {

    //Declare OpMode Members
    private ElapsedTime runtime = new ElapsedTime();

    //Declare Motor Controllers
    private DcMotorController redMage;
    private DcMotorController dalek;

    //Declare Motors
    private DcMotor backLeftMotor; //Port 1 On Controller dalek
    private DcMotor backRightMotor; //Port 2 On Controller dalek
    private DcMotor frontLeftMotor; //Port 1 On Controller redMage
    private DcMotor frontRightMotor; //Port 2 On Controller redMage

    //Declare Servo Motor Controllers
    private ServoController sonicScrewdriver;

    //Declare Servo Motors
    private Servo leftServo; //Port 1 on Controller sonicScrewdriver
    private Servo rightServo; //Port 2 on Controller sonicScrewdriver

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

        //HW Map Servos on Controller sonicScrewdriver
        leftServo = hardwareMap.servo.get("leftServo");
        rightServo = hardwareMap.servo.get("rightServo");

        //Reverse Direction of The Left Motors
        frontLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();
        runtime.reset();

        //Run until end of match (when driver presses stop)
        while (opModeIsActive()) {
            double leftPower;
            double rightPower;

            while (gamepad1.left_stick_x != 0 || gamepad1.left_stick_y != 0) {
                double drive = -gamepad1.left_stick_y;
                double turn = gamepad1.left_stick_x;

                leftPower = Range.clip(drive + turn, -1.0, 1.0);
                rightPower = Range.clip(drive - turn, -1.0, 1.0);

                frontLeftMotor.setPower(leftPower);
                frontRightMotor.setPower(rightPower);
                backLeftMotor.setPower(leftPower);
                backRightMotor.setPower(rightPower);
            }
            while (gamepad1.right_trigger > 0 || gamepad1.left_trigger > 0 || gamepad1.right_bumper) {

                while (gamepad1.right_trigger > 0 && !gamepad1.right_bumper) {
                    frontLeftMotor.setPower(gamepad1.right_trigger);
                    frontRightMotor.setPower(-gamepad1.right_trigger);
                    backLeftMotor.setPower(gamepad1.right_trigger);
                    backRightMotor.setPower(-gamepad1.right_trigger);
                }
                while (gamepad1.left_trigger > 0 && !gamepad1.right_bumper) {
                    frontLeftMotor.setPower(-gamepad1.left_trigger);
                    frontRightMotor.setPower(gamepad1.left_trigger);
                    backLeftMotor.setPower(-gamepad1.left_trigger);
                    backRightMotor.setPower(gamepad1.left_trigger);
                }

                while (gamepad1.right_bumper) {
                    while (gamepad1.right_trigger > 0) {
                        frontLeftMotor.setPower(gamepad1.right_trigger);
                        frontRightMotor.setPower(-gamepad1.right_trigger);
                        backLeftMotor.setPower(-gamepad1.right_trigger);
                        backRightMotor.setPower(gamepad1.right_trigger);
                    }

                    while (gamepad1.left_trigger > 0) {
                        frontLeftMotor.setPower(-gamepad1.left_trigger);
                        frontRightMotor.setPower(gamepad1.left_trigger);
                        backLeftMotor.setPower(gamepad1.left_trigger);
                        backRightMotor.setPower(-gamepad1.left_trigger);
                    }
                }
            }
            while (gamepad1.a) {
                leftServo.setPosition(0.5);
                rightServo.setPosition(0.5);
            }
            while (gamepad1.b) {
                leftServo.setPosition(0.0);
                rightServo.setPosition(1.0);
            }
            //if (gamepad1.left_stick_x == 0 && gamepad1.left_stick_y == 0 && gamepad1.left_trigger == 0 && gamepad1.right_trigger == 0)
            //{
                frontLeftMotor.setPower(0.0);
                frontRightMotor.setPower(0.0);
                backLeftMotor.setPower(0.0);
                backRightMotor.setPower(0.0);
            //}
            telemetry.addData("Status", "Running");
            telemetry.update();
            idle();
        }
    }
}
