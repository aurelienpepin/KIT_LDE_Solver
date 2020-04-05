# Solver of systems of linear diophantine equations

This repository features a solver of systems of linear diophantine equations, i.e. equations with integer coefficients and integer unknown variables.
It is based on a variant of the [Gauss elimination](https://en.wikipedia.org/wiki/Gaussian_elimination).

The implementation was developed as part of the course *Decision procedures with applications for software
verification* at Karlsruhe Institute of Technology ([see here](http://verialg.iti.kit.edu/685.php)). The underlying algorithm is
explained [here](http://verialg.iti.kit.edu/download/EAS-2018-11-07.pdf) (slides in German).

## Inputs

Systems of equations are represented as follows. If *n* is the number of equations and *m* is the number of variables, then each system
consists of *n + 1* lines. The first line contains *n* and *m*. Each of the following *n* lines is an equation. An equation is made
of several pairs of integers *(c i)*. Each pair of integers represents the term *c · x_i*. The last pair has the form *(d 0)* where *d*
is the value of the right-hand side of the equation.

See [here](http://verialg.iti.kit.edu/download/EAS-AB-2.pdf) for an example.

## Built with

* Java 8

## Authors

* [Aurélien Pepin](https:///github.com/aurelienpepin) – KIT, Ensimag

## See also

* [SMT LIA](http://smtlib.cs.uiowa.edu/logics-all.shtml#LIA)
