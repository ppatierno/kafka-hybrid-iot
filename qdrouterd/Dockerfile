FROM centos:centos7

RUN yum -y install epel-release && yum -y install qpid-dispatch-router qpid-dispatch-tools && yum clean all

CMD ["qdrouterd"]