
# RISC-V simulator in Java
This repository includes a basic implementation of the RISC-V architecture using 
the RV32I base integer instructions (R, I, S, B, U and J) - except `ebreak`.

The simulator reads, executes and prints the resulting registers using a
binary instruction file.

## Setup
To use this simulator, a Java-21 environment is recommended, 
earlier versions might work, but haven't been tested.

## Compiling and running
### Using make
To compile the simulator you should 
1. Navigate to the `./src` folder in your terminal.
2. Write `make` - which will compile and run the files

### Using archive (.jar)
To run precompiled executable:
1. Navigate to project folder in your terminal.
2. Write `java -jar RISC-V_Sim.jar` to run the simulator

### Running the file
- `Size of the memory in MB`: the amount of memory the CPU 
  has access to. Inputting `1` works for simple programs, 
  but larger memory could be necessary for larger programs.
- `Path to the binary file`: Uses your current terminal path.
  Navigating to the project folder can be done by writing
  `../path/to/binary/file.bin`
- `Should instructions be printed as they execute? (y/n)`: <br> 
  Input `y` if you also want to output the input in assembly. <br>
  Input `n` if you only want to output the resulting registers.
- The input binary will be run, and resulting registers will be printed.

This project is a fork of [cae-lab/finasgmt](https://github.com/schoeberl/cae-lab/tree/master/finasgmt).