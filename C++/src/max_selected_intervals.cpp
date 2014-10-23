/*
Author: Jiankai Sun
Date: 2014.10.21
*/
#include<iostream>
using namespace std;
//find the largest j which makes f[j]<=s[i]
int find_opt(double [], double [],int);
//return the maximum sum of the selected intervals
//non recurssive version
double max_select_nonrec(double [],double [],int);
//recurssive version
double max_select_rec(double [],double [],int);
//void print selected intervals
void print_interval(int *,int);

int find_opt(double s[],double f[],int i){
	for(int j=i-1;j>=0;j--){
		if(f[j]<=s[i]){
			return j;
		}
	}
	return 0;
}

double max_select_nonrec(double s[], double f[],int n){
	double * T=new double[n];
	int * P=new int[n];
	for(int i=0;i<n;i++){
		T[i]=0;
	}
	for(int i=1;i<n;i++){
		int j=find_opt(s,f,i);
		double temp=T[j]+f[i]-s[i];
		if(temp>=T[i-1]){
			T[i]=temp;
			P[i]=j;
		}
		else{
			T[i]=T[i-1];
			P[i]=-1;
		}
		
	}
	//print 
	print_interval(P,n);
	return T[n-1];
}
void print_interval(int * P, int n){
	int i=n-1;
	while(P[i]!=0){
		if(P[i]>0){
			cout<<i<<" ";
			i=P[i];
		}
	}
	//print the last one
	cout<<i<<endl;
}

double max_select_rec(double s[],double f[], int i){
	
	if(i<=0)
		return 0;
	else
		return max(max_select_rec(s,f,find_opt(s,f,i))+f[i]-s[i],max_select_rec(s,f,i-1));
}

int main(){
	int n=7;
	double s[]={0,1,5,6,7,15,1};
	double f[]={0,4,10,12,14,20,21};
	cout<<max_select_rec(s,f,n-1)<<endl;
	cout<<max_select_nonrec(s,f,n)<<endl;
	return 0;
}