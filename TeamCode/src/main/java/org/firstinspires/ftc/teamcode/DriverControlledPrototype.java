package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Sebastian on 10/30/2017.
 */

@TeleOp(name = "Gamepad Prototype -- IF THIS IS VISIBLE, THIS IS A BUG! RUN ULTIMA TELEOP INSTEAD!!!", group = "Linear OpMode")
@Disabled
public class DriverControlledPrototype extends LinearOpMode {

    //Declare OpMode Members
    private ElapsedTime runtime = new ElapsedTime();

    //Remove Once we get our first robot
    private DcMotor leftMotor = null;
    private DcMotor rightMotor = null;

    @Override
    public void runOpMode() throws InterruptedException {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        leftMotor = hardwareMap.dcMotor.get("leftMotor");
        rightMotor = hardwareMap.dcMotor.get("rightMotor");
        rightMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();
        runtime.reset();

            //Run until end of match (when driver presses stop)
            while (opModeIsActive()) {
                double leftPower;
                double rightPower;
                while (gamepad1.left_stick_x != 0 || gamepad1.left_stick_y != 0) {
                    double drive = -gamepad1.left_stick_y;
                    double turn = gamepad1.left_stick_x;

                    leftPower = Range.clip(drive - turn, -1.0, 1.0);
                    rightPower = Range.clip(drive + turn, -1.0, 1.0);

                    leftMotor.setPower(leftPower);
                    rightMotor.setPower(rightPower);
                }
                while (gamepad1.right_trigger > 0) {
                    leftMotor.setPower(-gamepad1.right_trigger);
                    rightMotor.setPower(gamepad1.right_trigger);
                }
                while (gamepad1.left_trigger > 0) {
                        leftMotor.setPower(gamepad1.left_trigger);
                        rightMotor.setPower(-gamepad1.left_trigger);
                }
                leftMotor.setPower(0.0);
                rightMotor.setPower(0.0);
                telemetry.addData("Status", "Running");
                telemetry.update();
                idle();
            }
        }

    }

