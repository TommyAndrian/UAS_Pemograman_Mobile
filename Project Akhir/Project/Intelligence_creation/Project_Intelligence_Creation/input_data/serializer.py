from rest_framework import serializers
from .models import Project,Dataset,Column,TrainingTestingResult,KomunikasiManajemen,ModelPlanning
from .models import DataProjek

class DataProjekSerializer(serializers.ModelSerializer):
    class Meta:
        model = DataProjek
        fields = ['id', 'nama_proyek']

class ProjectSerializer(serializers.Serializer):
    project_id = serializers.IntegerField()
    project_name = serializers.CharField(max_length=200)
    description = serializers.CharField(max_length=1000)
    algorithm = serializers.CharField(max_length=200)
    output = serializers.CharField(max_length=200)

class ProjectSerializer(serializers.ModelSerializer):
    class Meta:
        model = Project
        fields = '__all__'

    def update(self, instance, validated_data):
        instance.project_name = validated_data.get('project_name', instance.project_name)
        instance.description = validated_data.get('description', instance.description)
        instance.algorithm = validated_data.get('algorithm', instance.algorithm)
        instance.output = validated_data.get('output', instance.output)
        instance.save()
        return instance


class ColumnSerializer(serializers.ModelSerializer):
    class Meta:
        model = Column
        fields = [ 'dataset', 'name', 'data_type']

class DatasetSerializer(serializers.ModelSerializer):
    columns = ColumnSerializer(many=True, read_only=True)  # Nested serializer for columns

    class Meta:
        model = Dataset
        fields = [ 'name', 'nama_dataset', 'description', 'columns']



class TrainingTestingResultSerializer(serializers.ModelSerializer):
    project = serializers.SerializerMethodField()
    dataset = serializers.SerializerMethodField()
    activity = serializers.SerializerMethodField()

    class Meta:
        model = TrainingTestingResult
        fields = ['id', 'training_result', 'testing_result', 'project', 'dataset', 'activity']

    def get_project(self, obj):
        print(f"Serializing project: {obj.project.project_name}")
        return obj.project.project_name

    def get_dataset(self, obj):
        print(f"Serializing dataset: {obj.dataset.nama_dataset}")
        return obj.dataset.nama_dataset

    def get_activity(self, obj):
        print(f"Serializing activity: {obj.activity.model_name}")
        return obj.activity.model_name

class KomunikasiManajemenSerializer(serializers.ModelSerializer):
    class Meta:
        model = KomunikasiManajemen
        fields = '__all__'