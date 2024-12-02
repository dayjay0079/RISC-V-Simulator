
# RISC-V simulator in Java
This repository includes a basic implementation of the RISC-V architecture using 
the RV32I base integer instructions (R, I, S, B, U and J) - except `ebreak`.

The simulator reads, executes and prints the resulting registers using a
binary instruction file.

## Setup
To use this simulator, a Java-21 environment is recommended. Earlier versions might work, but haven't been tested.

## Compiling and running
A precompiled `.jar` archive is available in to project root, however the source code can be compiled by the user as well.

### Using javac
To compile the simulator via the java compiler, simply compile the `SimTop.java` file with the command `javac SimTop.java`.

### Using make
To compile the simulator via make, you should:
1. Navigate to the `./src` folder in your terminal.
2. Execute `make` - to will compile the files.

### Running the file
- To simulate a given binary file, it simply has to be entered as an argument when executing the simulator: `java SimTop <path-to-binary>` or `java -jar RISC-V_Sim.jar <path-to-binary>`.
- The input binary will be simulated, and resulting registers will be printed as well as dumped to the inputs directory as `<path-to-binary>_Sim.res`.

---

This project is a fork of [cae-lab/finasgmt](https://github.com/schoeberl/cae-lab/tree/master/finasgmt).