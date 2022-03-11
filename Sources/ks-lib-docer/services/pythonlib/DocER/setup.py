from distutils.core import setup

setup(
    name='DocER',
    version='1.0',
    author='Lorenzo Lucherini',
    author_email='lorenzo.lucherini@kdm.it',
    packages=['docer'],
#    url='http://pypi.python.org/pypi/TowelStuff/',
#    license='LICENSE.txt',
    description='Libreria per accesso a DocER da python.',
#    long_description=open('README.txt').read(),
    install_requires=[
        "yolk >= 0.4.1",
        "elementtree >= 1.2.7-20070827",
    ],
)
