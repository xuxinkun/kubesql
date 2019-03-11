from setuptools import setup, find_packages

setup(
    name='kubesql',
    version='0.0.2',
    author='xuxinkun',
    author_email='xuxinkun@gmail.com',
    classifiers=[
        'Operating System :: POSIX :: Linux',
        'Programming Language :: Python',
        'Programming Language :: Python :: 2',
        'Programming Language :: Python :: 2.7',
        'Programming Language :: Python :: 2.6'],
    packages=find_packages(),
    entry_points={'console_scripts':
                      ['kubesql-server = kubesql.server:main',
                       'kubesql = kubesql.client:main',
                       'kubesql-watch = kubesql.kubewatch:main'],
                  }
)
