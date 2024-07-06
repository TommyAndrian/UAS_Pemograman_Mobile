from django.shortcuts import render, redirect, get_object_or_404
from django.db.models import Q
from .models import Project, Dataset, Column
from .forms import ProjectForm, DatasetForm, ColumnFormSet
from .forms import ProsesForm
from .models import Proses
from .models import ModelPlanning 
from .forms import ModelPlanningForm
from .models import RefiningPlanning
from .forms import RefiningPlanningForm
from .forms import TrainingTestingResultForm
from .models import TrainingTestingResult
from .models import RefiningResult
from .forms import RefiningResultForm
from .models import KomunikasiManajemen
from .forms import KomunikasiManajemenForm
from .models import Dataset, Column
from .forms import DatasetForm, ColumnForm, ColumnFormSet
from .serializer import ProjectSerializer,DatasetSerializer,ColumnSerializer,TrainingTestingResultSerializer,KomunikasiManajemenSerializer
from rest_framework import generics
import requests
from django.http import JsonResponse
from django.shortcuts import render
import json
from django.http import JsonResponse,HttpResponseBadRequest,HttpResponseNotAllowed
from django.views.decorators.csrf import csrf_exempt
from django.shortcuts import get_object_or_404
from .models import DataProjek
import logging


logger = logging.getLogger(__name__)





class DatasetListCreateAPIView(generics.ListCreateAPIView):
    queryset = Dataset.objects.all()
    serializer_class = DatasetSerializer

class DatasetRetrieveUpdateDestroyAPIView(generics.RetrieveUpdateDestroyAPIView):
    queryset = Dataset.objects.all()
    serializer_class = DatasetSerializer

class ColumnListCreateAPIView(generics.ListCreateAPIView):
    queryset = Column.objects.all()
    serializer_class = ColumnSerializer

class ColumnRetrieveUpdateDestroyAPIView(generics.RetrieveUpdateDestroyAPIView):
    queryset = Column.objects.all()
    serializer_class = ColumnSerializer

class ProjectListCreateAPIView(generics.ListCreateAPIView):
    queryset = Project.objects.all()
    serializer_class = ProjectSerializer

class ProjectRetrieveUpdateDestroyAPIView(generics.RetrieveUpdateDestroyAPIView):
    queryset = Project.objects.all()
    serializer_class = ProjectSerializer

class TrainingTestingResultList(generics.ListAPIView):
    queryset = TrainingTestingResult.objects.all()
    serializer_class = TrainingTestingResultSerializer

class TrainingTestingResultDetail(generics.RetrieveAPIView):
    queryset = TrainingTestingResult.objects.all()
    serializer_class = TrainingTestingResultSerializer

class KomunikasiManajemenListCreate(generics.ListCreateAPIView):
    queryset = KomunikasiManajemen.objects.all()
    serializer_class = KomunikasiManajemenSerializer

class KomunikasiManajemenDetail(generics.RetrieveUpdateDestroyAPIView):
    queryset = KomunikasiManajemen.objects.all()
    serializer_class = KomunikasiManajemenSerializer


def status_form_view(request):
    # URL API untuk mendapatkan daftar proyek dari myapp1
    api_url = 'http://10.24.80.147:8004/api/projek-list/'

    # Ambil data dari API
    response = requests.get(api_url)

    # Periksa apakah permintaan berhasil
    if response.status_code == 200:
        projek_list = response.json()
        print("Data diterima dari API:", projek_list)  # Log struktur data
    else:
        projek_list = []
        print("Gagal mengambil data:", response.status_code, response.text)  # Log pesan kesalahan

    # Render template dengan data daftar proyek
    return render(request, 'input_data/status.html', {'projek_list': projek_list})

def project_view(request):
    # URL API untuk mendapatkan daftar proyek dari myapp1
    api_url = 'http://10.24.80.147:8004/api/projek-list/'

    # Ambil data dari API
    response = requests.get(api_url)

    # Periksa apakah permintaan berhasil
    if response.status_code == 200:
        data = response.json()
        projek_list = data  # Data berupa daftar proyek
        print("Data diterima dari API:", projek_list)  # Log data yang diterima
        
        # Simpan data ke dalam model DataProjek
        for projek in projek_list:
            try:
                print("Menyimpan proyek:", projek['nama_proyek'])  # Log proyek yang disimpan
                DataProjek.objects.update_or_create(
                    nama_proyek=projek['nama_proyek']
                )
            except KeyError:
                print("KeyError: 'nama_proyek' tidak ditemukan pada:", projek)
    else:
        projek_list = []
        print("Gagal mengambil data:", response.status_code, response.text)  # Log kesalahan

    # Ambil semua data dari DataProjek untuk ditampilkan di template
    saved_projects_data = DataProjek.objects.all()
    print("Proyek yang disimpan di database:", saved_projects_data)  # Log data yang disimpan di database
    return render(request, 'input_data/projekinfo.html', {
        'projek_list_data': saved_projects_data
    })

def post_status_view(request):
    if request.method == 'POST':
        # Ekstrak data dari permintaan POST
        projek_id = request.POST.get('projek')
        status = request.POST.get('status')

        # Validasi data
        if not projek_id or not status:
            return JsonResponse({'error': 'Data tidak valid. ID Proyek dan status diperlukan.'}, status=400)

        # URL untuk menyimpan status ke API Django Anda
        api_url = 'http://10.24.80.147:8004/api/status/'

        # Persiapkan data untuk permintaan POST
        data = {
            'projek': projek_id,
            'status': status,
        }

        # Kirim permintaan POST
        response = requests.post(api_url, json=data)  # Pastikan mengirim JSON

        # Periksa apakah permintaan berhasil
        if response.status_code == 201:  # Anggap kode status 201 menunjukkan pembuatan berhasil
            return JsonResponse({'message': 'Status berhasil dibuat!'})
        else:
            return JsonResponse({'error': 'Gagal membuat status.', 'details': response.json()}, status=response.status_code)

    # Tangani metode lain jika diperlukan
    return JsonResponse({'error': 'Metode permintaan tidak valid.'}, status=405)

def file_list(request):
    api_url = 'http://10.24.76.57:8002/api/files/'  # Replace with your actual API endpoint
    response = requests.get(api_url)

    if response.status_code == 200:
        data = response.json()  # Assuming the API returns JSON
        files = [{'file_name': item['file_name'],'my_file': item['my_file'], 'status': item['status']} for item in data]
    else:
        files = []

    return render(request, 'input_data/file_list.html', {'files': files})

def apiproject_list(request):
    # URL dari API
    url = 'http://10.24.80.147:8005/api/projects/'

    # Mengirim permintaan GET
    response = requests.get(url)

    api_projects = []
    if response.status_code == 200:
        data = response.json()
        api_projects = data.get('projects', [])

    # Mengambil data lokal dari database
    local_projects = Project.objects.all()

    # Kirim data ke template
    context = {
        'api_projects': api_projects,
        'local_projects': local_projects,
        'query': request.GET.get('q', '')
    }
    return render(request, 'input_data/apiproject_list.html', context)


def fetch_api_data(request):
    api_url = 'http://10.24.80.147:8005/pekerjaan/1/'
    response = requests.get(api_url)

    if response.status_code == 200:
        data = response.json()
        return render(request, 'input_data/update_pekerjaan.html', {'data': data})
    else:
        error_message = f"Tidak dapat mengambil data dari API: {response.status_code}"
        return render(request, 'error.html', {'error_message': error_message})

def update_status(request):
    if request.method == 'PATCH':
        api_url = 'http://10.24.80.147:8005/pekerjaan/1/'
        new_status = json.loads(request.body).get('status')
        
        headers = {
            'Content-Type': 'application/json',
            'X-CSRFToken': request.headers.get('X-CSRFToken'),
        }
        
        data = {
            'status': new_status
        }
        
        try:
            response = requests.patch(api_url, headers=headers, data=json.dumps(data))
            
            if response.status_code == 200:
                logger.info(f"Status berhasil diupdate: {response.json()}")
                return JsonResponse({'message': 'Status berhasil diupdate'}, status=200)
            else:
                logger.error(f"Gagal melakukan update status: {response.status_code}")
                return JsonResponse({'error': 'Gagal melakukan update status'}, status=response.status_code)
        
        except requests.exceptions.RequestException as e:
            logger.error(f"Gagal melakukan request: {str(e)}")
            return JsonResponse({'error': f'Gagal melakukan request: {str(e)}'}, status=500)
    else:
        return JsonResponse({'error': 'Metode request tidak valid'}, status=405)

# View untuk Project
def project_list(request):
    query = request.GET.get('q')
    if query:
        projects = Project.objects.filter(Q(project_name__icontains=query))
    else:
        projects = Project.objects.all()
    return render(request, 'input_data/project_list.html', {'projects': projects, 'query': query})

def project_create(request):
    if request.method == 'POST':
        form = ProjectForm(request.POST)
        if form.is_valid():
            form.save()
            return redirect('input_data:project_list')
    else:
        form = ProjectForm()
    return render(request, 'input_data/project_form.html', {'form': form})

def project_update(request, pk):
    project = get_object_or_404(Project, pk=pk)
    if request.method == 'POST':
        form = ProjectForm(request.POST, instance=project)
        if form.is_valid():
            form.save()
            return redirect('input_data:project_list')
    else:
        form = ProjectForm(instance=project)
    return render(request, 'input_data/project_form.html', {'form': form})

def project_delete(request, pk):
    project = get_object_or_404(Project, pk=pk)
    if request.method == 'POST':
        project.delete()
        return redirect('input_data:project_list')
    else:
        return HttpResponseNotAllowed(['POST'])

def project_detail(request, pk):
    project = get_object_or_404(Project, pk=pk)
    return render(request, 'input_data/project_detail.html', {'project': project})

def dataset_list(request):
    query = request.GET.get('q')
    if query:
        datasets = Dataset.objects.filter(Q(name__icontains=query))
    else:
        datasets = Dataset.objects.all()
    return render(request, 'input_data/dataset_list.html', {'datasets': datasets, 'query': query})

def dataset_create(request):
    if request.method == 'POST':
        dataset_form = DatasetForm(request.POST)
        if dataset_form.is_valid():
            dataset = dataset_form.save()
            return redirect('input_data:add_column', pk=dataset.pk)
    else:
        dataset_form = DatasetForm()

    return render(request, 'input_data/dataset_form.html', {'dataset_form': dataset_form})

def dataset_detail(request, pk):
    dataset = get_object_or_404(Dataset, pk=pk)
    columns = dataset.columns.all()
    return render(request, 'input_data/dataset_detail.html', {'dataset': dataset, 'columns': columns})

def dataset_update(request, pk):
    dataset = get_object_or_404(Dataset, pk=pk)
    if request.method == 'POST':
        dataset_form = DatasetForm(request.POST, instance=dataset)
        column_formset = ColumnFormSet(request.POST, request.FILES, instance=dataset)
        if dataset_form.is_valid() and column_formset.is_valid():
            dataset_form.save()
            column_formset.save()
            return redirect('input_data:dataset_list')
    else:
        dataset_form = DatasetForm(instance=dataset)
        column_formset = ColumnFormSet(instance=dataset)

    return render(request, 'input_data/dataset_form.html', {
        'dataset_form': dataset_form,
        'column_formset': column_formset,
    })

def dataset_delete(request, pk):
    dataset = get_object_or_404(Dataset, pk=pk)
    if request.method == 'POST':
        dataset.delete()
        return redirect('input_data:dataset_list')
    else:
        return HttpResponseNotAllowed(['POST'])

def add_column(request, pk):
    dataset = get_object_or_404(Dataset, pk=pk)
    if request.method == 'POST':
        column_form = ColumnForm(request.POST)
        if column_form.is_valid():
            column = column_form.save(commit=False)
            column.dataset = dataset
            column.save()
            return redirect('input_data:add_column', pk=dataset.pk)
    else:
        column_form = ColumnForm()

    columns = dataset.columns.all()
    return render(request, 'input_data/add_column.html', {'dataset': dataset, 'column_form': column_form, 'columns': columns})

def edit_column(request, pk):
    column = get_object_or_404(Column, pk=pk)
    if request.method == 'POST':
        column_form = ColumnForm(request.POST, instance=column)
        if column_form.is_valid():
            column_form.save()
            return redirect('add_column', pk=column.dataset.pk)
    else:
        column_form = ColumnForm(instance=column)

    return render(request, 'input_data/edit_column.html', {'column_form': column_form, 'column': column})

def delete_column(request, pk):
    column = get_object_or_404(Column, pk=pk)
    dataset_pk = column.dataset.pk
    column.delete()
    return redirect('add_column', pk=dataset_pk)


def proses_create(request):
    if request.method == 'POST':
        form = ProsesForm(request.POST, request.FILES)
        if form.is_valid():
            form.save()
            return redirect('input_data:proses_list')
    else:
        form = ProsesForm()
    
    return render(request, 'input_data/proses_form.html', {'form': form})

def proses_list(request):
    proses_list = Proses.objects.all()
    return render(request, 'input_data/proses_list.html', {'proses_list': proses_list})

def proses_update(request, pk):
    proses = get_object_or_404(Proses, pk=pk)
    if request.method == 'POST':
        form = ProsesForm(request.POST, request.FILES, instance=proses)
        if form.is_valid():
            form.save()
            return redirect('input_data:proses_list')
    else:
        form = ProsesForm(instance=proses)
    
    return render(request, 'input_data/proses_form.html', {'form': form})

def proses_delete(request, pk):
    proses = get_object_or_404(Proses, pk=pk)
    if request.method == 'POST':
        proses.delete()
        return redirect('input_data:proses_list')
    else:
        return HttpResponseNotAllowed(['POST'])

def proses_detail(request, pk):
    proses = get_object_or_404(Proses, pk=pk)
    return render(request, 'input_data/proses_detail.html', {'proses': proses})

def proses_status_update(request, pk, status):
    proses = get_object_or_404(Proses, pk=pk)
    proses.status = status
    proses.save()
    return redirect('input_data:proses_list')

def model_planning_list(request):
    query = request.GET.get('q')
    if query:
        models = ModelPlanning.objects.filter(Q(model_name__icontains=query))
    else:
        models=ModelPlanning.objects.all()
    return render(request, 'input_data/model_planning_list.html', {'models': models, 'query': query})


def model_planning_create(request):
    if request.method == 'POST':
        form = ModelPlanningForm(request.POST)
        if form.is_valid():
            form.save()
            return redirect('input_data:model_planning_list')
    else:
        form = ModelPlanningForm()
    return render(request, 'input_data/model_planning_form.html', {'form': form})

def model_planning_update(request, pk):
    model = get_object_or_404(ModelPlanning, pk=pk)
    if request.method == 'POST':
        form = ModelPlanningForm(request.POST, instance=model)
        if form.is_valid():
            form.save()
            return redirect('input_data:model_planning_list')
    else:
        form = ModelPlanningForm(instance=model)
    return render(request, 'input_data/model_planning_form.html', {'form': form})

def model_planning_delete(request, pk):
    model = get_object_or_404(ModelPlanning, pk=pk)
    if request.method == 'POST':
        model.delete()
        return redirect('input_data:model_planning_list')
    else:
        return HttpResponseNotAllowed(['POST'])
    
def model_planning_detail(request, pk):
    model = get_object_or_404(ModelPlanning, pk=pk)
    return render(request, 'input_data/model_planning_detail.html', {'model': model})

def refining_planning_create(request):
    if request.method == 'POST':
        form = RefiningPlanningForm(request.POST)
        if form.is_valid():
            form.save()
            return redirect('input_data:refining_planning_list')
    else:
        form = RefiningPlanningForm()
    return render(request, 'input_data/refining_planning_form.html', {'form': form})

def refining_planning_update(request, pk):
    plan = get_object_or_404(RefiningPlanning, pk=pk)
    if request.method == 'POST':
        form = RefiningPlanningForm(request.POST, instance=plan)
        if form.is_valid():
            form.save()
            return redirect('input_data:refining_planning_list')
    else:
        form = RefiningPlanningForm(instance=plan)
    return render(request, 'input_data/refining_planning_form.html', {'form': form})

def refining_planning_delete(request, pk):
    plan = get_object_or_404(RefiningPlanning, pk=pk)
    if request.method == 'POST':
        plan.delete()
        return redirect('input_data:refining_planning_list')
    else:
        return HttpResponseNotAllowed(['POST'])

def refining_planning_detail(request, pk):
    plan = get_object_or_404(RefiningPlanning, pk=pk)
    return render(request, 'input_data/refining_planning_detail.html', {'plan': plan})

def refining_planning_list(request):
    query = request.GET.get('q')
    plans = RefiningPlanning.objects.all()
    if query:
        plans = plans.filter(model_name__icontains=query)
    return render(request, 'input_data/refining_planning_list.html', {'plans': plans, 'query': query})

def training_testing_result_create(request):
    if request.method == 'POST':
        form = TrainingTestingResultForm(request.POST, request.FILES)
        if form.is_valid():
            form.save()
            return redirect('input_data:training_testing_result_list')  # Redirect to the list view after saving
    else:
        form = TrainingTestingResultForm()
    return render(request, 'input_data/training_testing_result_form.html', {'form': form})
    

def training_testing_result_list(request):
    query = request.GET.get('q')
    results = TrainingTestingResult.objects.all()
    if query:
        results = results.filter(project__icontains=query)
    return render(request, 'input_data/training_testing_result_list.html', {'results': results,'query': query})

def training_testing_result_update(request, pk):
    result = get_object_or_404(TrainingTestingResult, pk=pk)
    if request.method == 'POST':
        form = TrainingTestingResultForm(request.POST, request.FILES, instance=result)
        if form.is_valid():
            form.save()
            return redirect('input_data:training_testing_result_list')
    else:
        form = TrainingTestingResultForm(instance=result)
    return render(request, 'input_data/training_testing_result_form.html', {'form': form})

def training_testing_result_delete(request, pk):
    result = get_object_or_404(TrainingTestingResult, pk=pk)
    if request.method == 'POST':
        result.delete()
        return redirect('input_data:training_testing_result_list')
    else:
        return HttpResponseNotAllowed(['POST'])

def training_testing_result_detail(request, pk):
    result = get_object_or_404(TrainingTestingResult, pk=pk)
    return render(request, 'input_data/training_testing_result_detail.html', {'result': result})

def refining_result_list(request):
    query = request.GET.get('q')
    refining_results = RefiningResult.objects.all()
    if query:
        refining_results = RefiningResult.objects.filter(Q(project__icontains=query))
    return render(request, 'input_data/refining_result_list.html', {'refining_results': refining_results,'query':query})

def refining_result_detail(request, pk):
    refining_result = get_object_or_404(RefiningResult, pk=pk)
    return render(request, 'input_data/refining_result_detail.html', {'refining_result': refining_result})

def refining_result_create(request):
    if request.method == 'POST':
        form = RefiningResultForm(request.POST, request.FILES)
        if form.is_valid():
            form.save()
            return redirect('input_data:refining_result_list')
    else:
        form = RefiningResultForm()
    return render(request, 'input_data/refining_result_form.html', {'form': form})

    

def refining_result_update(request, pk):
    refining_result = get_object_or_404(RefiningResult, pk=pk)
    if request.method == 'POST':
        form = RefiningResultForm(request.POST, request.FILES, instance=refining_result)
        if form.is_valid():
            form.save()
            return redirect('input_data:refining_result_list')
    else:
        form = RefiningResultForm(instance=refining_result)
    return render(request, 'input_data/refining_result_form.html', {'form': form})

def refining_result_delete(request, pk):
    refining_result = get_object_or_404(RefiningResult, pk=pk)
    if request.method == 'POST':
        refining_result.delete()
        return redirect('input_data:refining_result_list')
    else:
        return HttpResponseNotAllowed(['POST'])

def komunikasi_manajemen_list(request):
    query = request.GET.get('q')
    komunikasi_manajemens = KomunikasiManajemen.objects.all()
    if query:
        komunikasi_manajemens = KomunikasiManajemen.objects.filter(Q(nama_proyek__icontains=query))
    return render(request, 'input_data/komunikasi_manajemen_list.html', {'komunikasi_manajemens': komunikasi_manajemens, 'query': query})

def komunikasi_manajemen_detail(request, pk):
    komunikasi_manajemen = get_object_or_404(KomunikasiManajemen, pk=pk)
    return render(request, 'input_data/komunikasi_manajemen_detail.html', {'komunikasi_manajemen': komunikasi_manajemen})

def komunikasi_manajemen_create(request):
    if request.method == 'POST':
        form = KomunikasiManajemenForm(request.POST)
        if form.is_valid():
            form.save()
            return redirect('input_data:komunikasi_manajemen_list')
    else:
        form = KomunikasiManajemenForm()
    
    return render(request, 'input_data/komunikasi_manajemen_form.html', {'form': form})

def komunikasi_manajemen_update(request, pk):
    komunikasi_manajemen = get_object_or_404(KomunikasiManajemen, pk=pk)
    if request.method == 'POST':
        form = KomunikasiManajemenForm(request.POST, instance=komunikasi_manajemen)
        if form.is_valid():
            form.save()
            return redirect('input_data:komunikasi_manajemen_list')
    else:
        form = KomunikasiManajemenForm(instance=komunikasi_manajemen)
    
    return render(request, 'input_data/komunikasi_manajemen_form.html', {'form': form})

def komunikasi_manajemen_delete(request, pk):
    if request.method == 'POST':
        komunikasi_manajemen = get_object_or_404(KomunikasiManajemen, pk=pk)
        komunikasi_manajemen.delete()
        return redirect('input_data:komunikasi_manajemen_list')
    else:
        return HttpResponseNotAllowed(['POST'])