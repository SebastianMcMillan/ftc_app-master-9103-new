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


@TeleOp(name = "raceBot", group = "Linear OpMode")

public class raceBot extends LinearOpMode {

    //Declare OpMode Members
    private ElapsedTime runtime = new ElapsedTime();

    //Declare Motor Controllers
    private DcMotorController right;
    private DcMotorController left;

    //Declare Motors
    private DcMotor LeftMotor; //Port 1 On Controller dalek
    private DcMotor RightMotor; //Port 2 On Controller dalek

    @Override
    public void runOpMode() throws InterruptedException {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        //HW Map Motors on Controller redMage
        LeftMotor = hardwareMap.dcMotor.get("LeftMotor");
        RightMotor = hardwareMap.dcMotor.get("RightMotor");

        LeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        RightMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        double leftPower;
        double rightPower;


        waitForStart();
        runtime.reset();

        //Run until end of match (when driver presses stop)
        while (opModeIsActive()) {


            if (gamepad1.left_stick_x != 0 || gamepad1.left_stick_y != 0) /*If the left stick is not neutral...*/ {
                double drive = -gamepad1.left_stick_y; //Set the Drive to the negative value of the y-axis value
                double turn = gamepad1.left_stick_x; //Set the turn to the value of the x-axis value


                leftPower = Range.clip(drive + turn, -1.0, 1.0); //fun math
                rightPower = Range.clip(drive - turn, -1.0, 1.0); //fun math 2


                LeftMotor.setPower(leftPower); //These are hopefully self-explanatory.
                RightMotor.setPower(rightPower);

            }


            //If The Left Stick and the Triggers are not being pressed, Stop 'dem motors
            if (gamepad1.left_stick_x == 0 && gamepad1.left_stick_y == 0 && gamepad1.left_trigger == 0 && gamepad1.right_trigger == 0) {
                LeftMotor.setPower(0.0);
                RightMotor.setPower(0.0);

            }


        }
    }
}


