package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.CRServo;

@TeleOp(name = "Basic Drive", group = "Iterative Opmode")

public class BasicDrive extends OpMode {
    private DcMotor backLeft;
    private DcMotor backRight;
    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor clawArm;

    private Servo gripServo;

    private double deadzoneX = 0;
    private double deadzoneY = 0;
    private double deadzoneRotate = 0;

    @Override
    public void init() {
        telemetry.addData("Status", "Initializing");

        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        backLeft.setDirection(DcMotor.Direction.FORWARD);

        backRight = hardwareMap.get(DcMotor.class, "backRight");
        backRight.setDirection(DcMotor.Direction.REVERSE);

        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontLeft.setDirection(DcMotor.Direction.FORWARD);

        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        frontRight.setDirection(DcMotor.Direction.REVERSE);

        clawArm = hardwareMap.get(DcMotor.class, "clawArm");

        gripServo = hardwareMap.get(Servo.class, "gripServo");

        telemetry.addData("Status", "Initialized");
        telemetry.update();
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {

    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {

    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {
        if (Math.abs(gamepad1.left_stick_x) < 0.05) {
            deadzoneX = 0;
        } else {
            deadzoneX = -gamepad1.left_stick_x;
        }

        if (Math.abs(gamepad1.right_stick_x) < 0.05) {
            deadzoneY = 0;
        } else {
            deadzoneY = -gamepad1.right_stick_x;
        }

        if (Math.abs(gamepad1.left_stick_y) < 0.05) {
            deadzoneRotate = 0;
        } else {
            deadzoneRotate = gamepad1.left_stick_y;
        }

        // Servo Code
        if (gamepad1.b) {
            gripServo.setPosition(0.25); // Need to Adjust Values
        }
        if (gamepad1.a) {
            gripServo.setPosition(0.5); // Need to Adjust Values
        }

        // Claw Arm Code
        if (gamepad1.left_trigger >= 0.05)
            clawArm.setPower(-0.5);
        else if (gamepad1.right_trigger >= 0.05)
            clawArm.setPower(0.5);
        else
            clawArm.setPower(0);

        if (gamepad1.x)
            clawArm.setPower(-0.35);

        if (gamepad1.y)
            clawArm.setPower(0.35);

        // Mecanum wheel drive calculations
        deadzoneX *= Math.pow(Math.abs(deadzoneX), 2);
        deadzoneY *= Math.pow(Math.abs(deadzoneY), 2);

        double r = Math.hypot(deadzoneX, -deadzoneY); // deadzones are incorporated into these values
        double robotAngle = Math.atan2(-deadzoneY, deadzoneX) - Math.PI / 4;
        double rightX = deadzoneRotate / 1.25;
        final double v1 = r * Math.cos(robotAngle) + rightX;
        final double v2 = r * Math.sin(robotAngle) - rightX;
        final double v3 = r * Math.sin(robotAngle) + rightX;
        final double v4 = r * Math.cos(robotAngle) - rightX;

        // Power set to each motor based on these calculations
        frontRight.setPower((-1)*v1);
        frontLeft.setPower(v2);
        backRight.setPower((-1)*v3);
        backLeft.setPower(v4);

        if (!(gamepad1.left_trigger >= 0.05) && !(gamepad1.right_trigger >= 0.05) && !gamepad1.x && !gamepad1.y) {
            clawArm.setPower(0.1); // Adjust the power value as needed to counteract gravity
        }

        // Telemetry used for testing encoder values.
        telemetry.addData("frontLeftPosition", frontLeft.getCurrentPosition());
        telemetry.addData("backLeftPosition", backLeft.getCurrentPosition());
        telemetry.addData("frontRightPosition", frontRight.getCurrentPosition());
        telemetry.addData("backRightPosition", backRight.getCurrentPosition());
    }


    @Override
    public void stop() {

    }
}