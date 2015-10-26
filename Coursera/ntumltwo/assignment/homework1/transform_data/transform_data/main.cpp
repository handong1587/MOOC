#include <stdio.h>
#include <stdlib.h>

int main()
{
	FILE *fp = fopen("features.train.txt", "r");
	FILE *fp2 = fopen("features_2_vs_not_2.train", "w+");
	double digit, intensity, symmetry;
	while (fscanf(fp, "%lf %lf %lf", &digit, &intensity, &symmetry) != EOF)
	{
		fprintf(fp2, "%d 1:%lf 2:%lf\n", ((int)digit) == 2 ? 0 : 1, intensity, symmetry);
	}
	fclose(fp);
	fclose(fp2);
	return 0;
}