# forms.py
from django import forms
from .models import Project,Dataset, Column
from django.forms import inlineformset_factory
from .models import Proses
from .models import ModelPlanning
from .models import RefiningPlanning
from .models import TrainingTestingResult
from .models import RefiningResult

from .models import KomunikasiManajemen


class ProjectForm(forms.ModelForm):
    class Meta:
        model = Project
        fields = ['project_id', 'project_name', 'description', 'algorithm', 'output']

class DatasetForm(forms.ModelForm):
    class Meta:
        model = Dataset
        fields = ['name', 'description', 'nama_dataset']

class ColumnForm(forms.ModelForm):
    class Meta:

        model = Column
        fields = ['name', 'data_type']

ColumnFormSet = inlineformset_factory(Dataset, Column, form=ColumnForm, extra=1, can_delete=True)



class ProsesForm(forms.ModelForm):
    class Meta:
        model = Proses
        fields = ['nama_aktivitas', 'deskripsi_aktivitas', 'dataset_yang_dipakai', 'result_file', 'status']  

    def __init__(self, *args, **kwargs):
        super(ProsesForm, self).__init__(*args, **kwargs)
        self.fields['status'].choices = [('Selesai', 'Selesai'), ('Sedang Proses', 'Sedang Proses'), ('Belum Selesai', 'Belum Selesai')]
        self.fields['result_file'].widget.attrs['accept'] = '.csv, application/vnd.ms-excel, application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'

class ModelPlanningForm(forms.ModelForm):
    class Meta:
          
        model = ModelPlanning 
        fields = [
            'model_name',
            'deskripsi_model',
            'tujuan_model',
            'nama_algoritma',
            'kebutuhan_data',
            'metodologi_pengujian',
            'metode_pengukuran',
        ]



class RefiningPlanningForm(forms.ModelForm):
    class Meta:
        model = RefiningPlanning
        fields = [
            'model_name',
            'description',
            'refining_goal',
            'refining_strategy',
            'additional_data',
            'evaluation_methodology',
            'measurement_method',
            'evaluasi_model_awal',
        ]



class TrainingTestingResultForm(forms.ModelForm):
    class Meta:
        model = TrainingTestingResult
        fields = ['project', 'dataset', 'activity', 'training_result', 'testing_result']

class RefiningResultForm(forms.ModelForm):
    class Meta:
        model = RefiningResult
        fields = ['project', 'dataset', 'activity', 'refining_result']


class KomunikasiManajemenForm(forms.ModelForm):
    class Meta:
        model = KomunikasiManajemen
        fields = ['status']