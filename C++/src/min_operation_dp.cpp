#include<iostream>
using namespace std;
/*
Author: Jiankai Sun
Date: 2014/10/14
*/
/*
Let A=a1a2...am and B=b1b2...bn be two strings of characers. we
want to transform A into B using following operations:
delete a character
and a character
change a character
write a dynamic programming algorithm that finds the minimum number
of operations needed to transform A into B
*/
int min_ope(char * c1,const int m, char *c2,const int n){

	int F[m+1][n+1];//define operation matrix
	
	//boundary condition
	for(int i=0;i<=m;i++){
		F[i][0]=i;		
	}
	for(int i=0;i<=n;i++){
		F[0][i]=i;
	}
	for(int i=0;i<m;i++){
		for(int j=0;j<n;j++){
			//the index of F starts from [1,1] to store c1[0] and c2[0]
			int k=i+1;
			int l=j+1;
			if(c1[i]==c2[j])
				//equal, don't need to change
				F[k][l]=F[k-1][l-1];
			else{
				// add, delete or change operation
				F[k][l]=min(min(F[k-1][l-1]+1,F[k-1][l]+1),F[k][l-1]+1);
			}
			
		}
	}
	return F[m][n];
}

int main(){
int const m=2;
int const n=1;
char c1[m]={'a','c'};
char c2[n]={'b'};

cout<<min_ope(c1,m,c2,n)<<endl;

return 0;
}