#include<iostream>
using namespace std;
/*
Author: Jiankai Sun
Date: 2014.10.15
*/
/*
N jobs are to be scheduled for processing on one machine. Job i, 1 ≤ i ≤ N,
needs ti units of processing time. If job i is finished by time T, where T is a given
deadline, then a profit pi is earned; otherwise, a penalty qi
is imposed. (Both pi and qi are positive integers.) We want to select a subset S of jobs such that
(i) ∑i∈S ti ≤ T, and (ii) f(S) = ∑i∈S pi −∑i /∈S qi is maximum.
Show how to find such a set of jobs using dynamic programming.
*/
int const N=3;
int const T=6;
//profit
int p[N]={12,3,4};
//penalty
int q[N]={1,4,2};
//time consuming
int t[N]={1,4,3};
//net profit table
int f[N+1][T+1];
//pointer to index which job should be scheduled
int P[N+1][T+1];
	
void findMaxNetProfit(){
	//non recursive implementation
	for(int i=0;i<N;i++){
		for(int j=0;j<T;j++){
			//k is the job index; from 1 to N
			int k=i+1;
			// j is the time unit; form 1 to T
			int l=j+1;
			if(t[i]>l){
				f[k][l]=f[k-1][l]-q[i];
			}
			else{
				f[k][l]=max(f[k-1][l]-q[i],f[k-1][l-t[i]]+p[i]);
				if(f[k][l]==f[k-1][l-t[i]]+p[i]){
					P[k][l]=1;
				}
			}
		}
	}
	cout<<"net profit is "<<f[N][T]<<endl;
}
int findMaxNetProfit_rec(int job_n,int deadline){
	//recursive implementation 
	if(job_n==0){
		//boundary condition
		return 0;
	}
	if(t[job_n-1]>deadline){
		return findMaxNetProfit_rec(job_n-1,deadline)-q[job_n-1];
	}
	else{
		int a=findMaxNetProfit_rec(job_n-1,deadline)-q[job_n-1];
		int b=findMaxNetProfit_rec(job_n-1,deadline-t[job_n-1])+p[job_n-1];
		if(b>a)
			P[job_n][deadline]=1;
		return max(a,b);
	}

}
void printJobSet(){
	int l=T;
	for(int i=N;i>=1;i--){
			if(P[i][l]==1){
				cout<<"job "<<i<<" is in job set"<<endl;
				l=l-t[i-1];
			}
	}
}
int main(){
	
	for(int i=0;i<i+1;i++)
		f[0][0]=0;
	for(int i=0;i<N;i++){
		for(int j=0;j<T;j++){
			P[i][j]=0;
		}
	}
	//findMaxNetProfit();
	cout<<findMaxNetProfit_rec(N,T)<<endl;
	printJobSet();
	return 0;
}