import Gnuplot 
import numpy as np

point1 = 8704/(1*8704) 
point2 = 8704/(2*1774.6)
point3 = 8704/(3*2028)

x = np.linspace(0,1,3)

g = Gnuplot.Gnuplot(persist=1)

d1 = Gnuplot.Data(point1, point2, point3, title='Fibonacci')
g('set grid')
g('set key left ')

g.plot(d1)