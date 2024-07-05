from django.db import models
from django.core.validators import FileExtensionValidator

class DataProjek(models.Model):
    
    nama_proyek = models.CharField(max_length=200, null=False)

    def __str__(self):
        return self.nama_proyek


class Pekerjaan(models.Model):
    status = models.CharField(max_length=100)
    def __str__(self):
        return self.status

class Project(models.Model):
    project_id = models.CharField(max_length=100, null=True, blank=True)
    project_name = models.CharField(max_length=100, null=True, blank=True)
    description = models.TextField(max_length=250, null=True, blank=True)
    algorithm = models.CharField(max_length=50, null=True, blank=True)
    output = models.TextField(max_length=100, null=True, blank=True)

    def __str__(self):
        return self.project_name

class Dataset(models.Model):
    name = models.ForeignKey(Project, on_delete=models.CASCADE, related_name='datasets')
    nama_dataset = models.CharField(max_length=100, null=True, blank=True)
    description = models.TextField()

    def __str__(self):
        return self.nama_dataset or "Unnamed Dataset"

class Column(models.Model):
    dataset = models.ForeignKey(Dataset, related_name='columns', on_delete=models.CASCADE)
    name = models.CharField(max_length=255)
    data_type = models.CharField(max_length=255)

    def __str__(self):
        return self.name


class Proses(models.Model):
    nama_aktivitas = models.Field()
    deskripsi_aktivitas = models.TextField()
    dataset_yang_dipakai = models.ForeignKey('Dataset', on_delete=models.CASCADE)
    result_file = models.FileField(upload_to='activity_results/')

    STATUS_CHOICES = [
        ('Belum Selesai', 'Belum Selesai'),
        ('Sedang Proses', 'Sedang Proses'),
        ('Selesai', 'Selesai'),
    ]
    status = models.CharField(max_length=20, choices=STATUS_CHOICES, default='Belum Selesai')

    def __str__(self):
        return self.nama_aktivitas

class ModelPlanning(models.Model):
    model_name = models.CharField(max_length=100)
    deskripsi_model = models.TextField(max_length=100, null=True, blank=True)
    tujuan_model = models.TextField(max_length=100, null=True, blank=True)
    nama_algoritma = models.CharField(max_length=100, null=True, blank=True)
    kebutuhan_data = models.TextField(max_length=100, null=True, blank=True)
    metodologi_pengujian = models.TextField(max_length=100, null=True, blank=True)
    metode_pengukuran = models.TextField(max_length=100, null=True, blank=True)

    def __str__(self):
        return self.model_name

class RefiningPlanning(models.Model):
    model_name = models.ForeignKey(ModelPlanning, on_delete=models.CASCADE, related_name='refinings')
    description = models.TextField()
    refining_goal = models.TextField()
    refining_strategy = models.CharField(max_length=100)
    additional_data = models.TextField()
    evaluation_methodology = models.TextField()
    measurement_method = models.TextField()
    evaluasi_model_awal = models.TextField(max_length=100, null=True, blank=True)

    def __str__(self):
        return str(self.model_name)

class TrainingTestingResult(models.Model):
    project = models.ForeignKey(Project, on_delete=models.CASCADE)
    dataset = models.ForeignKey(Dataset, on_delete=models.CASCADE)
    activity = models.ForeignKey(ModelPlanning, on_delete=models.CASCADE)
    training_result = models.FileField(upload_to='training_results/')
    testing_result = models.FileField(upload_to='testing_results/')
      

class RefiningResult(models.Model):
    project = models.ForeignKey(Project, on_delete=models.CASCADE)
    dataset = models.ForeignKey(Dataset, on_delete=models.CASCADE)
    activity = models.ForeignKey(ModelPlanning, on_delete=models.CASCADE)
    refining_result = models.FileField(upload_to='refining_results/')
class KomunikasiManajemen(models.Model):
    status = models.TextField(max_length=250, null=True, blank=True)

    def __str__(self):
        return self.status if self.status else "No Status"
