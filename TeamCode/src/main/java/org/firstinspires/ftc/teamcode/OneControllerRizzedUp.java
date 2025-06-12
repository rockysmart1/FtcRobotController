package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.JavaUtil;

@TeleOp(name = "OneControllerRizzedUp")
public class OneControllerRizzedUp extends LinearOpMode {

    // PRIVATE HARDWARE DECLARATIONS - Best practice for encapsulation
    private DcMotor leftFront;
    private DcMotor rightFront;
    private DcMotor leftRear;
    private DcMotor rightRear;
    private DcMotor armMotor;
    private Servo servo;

    // Private variables for claw state
    private boolean clawOpen = true;
    private boolean clawPressed = false;

    @Override
    public void runOpMode() {
        double speedE;
        double forward;
        double strafe;
        double turn;
        double denominator;
        double armPowerVariable;
        double arm_power;

        // Initialize hardware - assign to private variables
        servo = hardwareMap.get(Servo.class, "arm servo!");
        armMotor = hardwareMap.get(DcMotor.class, "Arm Motor");
        leftFront = hardwareMap.get(DcMotor.class, "leftFront");
        rightFront = hardwareMap.get(DcMotor.class, "rightFront");
        leftRear = hardwareMap.get(DcMotor.class, "leftRear");
        rightRear = hardwareMap.get(DcMotor.class, "rightRear");

        // CRITICAL: Set motor directions to fix drifting/skirting
        // These directions depend on how your motors are mounted
        // Try this configuration first - adjust if needed
        leftFront.setDirection(DcMotor.Direction.REVERSE);
        rightFront.setDirection(DcMotor.Direction.FORWARD);
        leftRear.setDirection(DcMotor.Direction.REVERSE);
        rightRear.setDirection(DcMotor.Direction.FORWARD);

        // Put initialization blocks here.
        waitForStart();
        // 0.2 before 0.1 to try make open wider
        servo.setPosition(0.1);

        if (opModeIsActive()) {
            while (opModeIsActive()) {

                //set speed
                if (gamepad1.right_bumper) {
                    armPowerVariable = 0.15;
                    speedE = 0.25;
                } else {
                    speedE = 1;
                    armPowerVariable = 2;
                }

                //servo
                if ((gamepad1.a && !clawPressed)) {
                    telemetry.addData("a pressed:", true);
                    clawOpen = !clawOpen;
                    if (clawOpen) {
                        servo.setPosition(0.35);
                    } else {
                        servo.setPosition(0.25);
                    }
                }
                clawPressed = gamepad1.a;

                // arm movement
                if(gamepad1.dpad_up) {
                    arm_power = 0.4 * armPowerVariable;
                } else if (gamepad1.dpad_down) {
                    arm_power = -0.25 * armPowerVariable;
                } else {
                    arm_power = 0;
                }

                armMotor.setPower(arm_power);
                telemetry.addData("armPosition:", armMotor.getCurrentPosition());

                // MECANUM MOVEMENT CONTROLS - DEBUGGING VERSION
                // Joystick A Y-axis (left_stick_y) → Forward/Backward
                // Joystick A X-axis (left_stick_x) → Turning
                // Joystick B X-axis (right_stick_x) → Side-to-side strafe

                forward = -gamepad1.left_stick_y * speedE;  // Negative because Y-axis is inverted
                turn = gamepad1.left_stick_x * speedE;     // Left stick X for turning
                strafe = gamepad1.right_stick_x * speedE;  // Right stick X for strafe

                // Calculate denominator to normalize power values
                denominator = JavaUtil.maxOfList(JavaUtil.createListWith(1, Math.abs(forward) + Math.abs(strafe) + Math.abs(turn)));

                // SIMPLIFIED MECANUM CALCULATIONS - Try this version first
                double leftFrontPower = (forward + strafe + turn) / denominator;
                double rightFrontPower = (forward - strafe - turn) / denominator;
                double leftRearPower = (forward - strafe + turn) / denominator;
                double rightRearPower = (forward + strafe - turn) / denominator;

                // Apply power to motors
                leftFront.setPower(leftFrontPower);
                rightFront.setPower(rightFrontPower);
                leftRear.setPower(leftRearPower);
                rightRear.setPower(rightRearPower);

                // DETAILED TELEMETRY for debugging
                telemetry.addData("=== JOYSTICK INPUTS ===", "");
                telemetry.addData("Left Stick Y (forward):", gamepad1.left_stick_y);
                telemetry.addData("Left Stick X (turn):", gamepad1.left_stick_x);
                telemetry.addData("Right Stick X (strafe):", gamepad1.right_stick_x);
                telemetry.addData("=== CALCULATED VALUES ===", "");
                telemetry.addData("Forward:", "%.2f", forward);
                telemetry.addData("Turn:", "%.2f", turn);
                telemetry.addData("Strafe:", "%.2f", strafe);
                telemetry.addData("=== MOTOR POWERS ===", "");
                telemetry.addData("Left Front:", "%.2f", leftFrontPower);
                telemetry.addData("Right Front:", "%.2f", rightFrontPower);
                telemetry.addData("Left Rear:", "%.2f", leftRearPower);
                telemetry.addData("Right Rear:", "%.2f", rightRearPower);
                telemetry.update();
            }
        }
    }
}